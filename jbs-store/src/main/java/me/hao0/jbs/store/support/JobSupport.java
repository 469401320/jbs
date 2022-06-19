package me.hao0.jbs.store.support;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import me.hao0.jbs.common.dto.JobDetail;
import me.hao0.jbs.common.dto.JobFireTime;
import me.hao0.jbs.common.dto.JobInstanceWaitResp;
import me.hao0.jbs.common.dto.ShardFinishDto;
import me.hao0.jbs.common.exception.JobStateTransferInvalidException;
import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.model.App;
import me.hao0.jbs.common.model.Job;
import me.hao0.jbs.common.model.JobInstance;
import me.hao0.jbs.common.model.enums.JobInstanceShardStatus;
import me.hao0.jbs.common.model.enums.JobInstanceStatus;
import me.hao0.jbs.common.model.enums.JobState;
import me.hao0.jbs.common.retry.Retryer;
import me.hao0.jbs.common.retry.Retryers;
import me.hao0.jbs.common.support.SimpleJobStateMachine;
import me.hao0.jbs.common.util.*;
import me.hao0.jbs.common.zk.Lock;
import me.hao0.jbs.common.zk.NodeListener;
import me.hao0.jbs.common.zk.NodeWatcher;
import me.hao0.jbs.store.dao.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


@Component
public class JobSupport implements DisposableBean {

    @Autowired
    private JbsZkClient zk;

    @Autowired
    private AppDao appDao;

    @Autowired
    private JobDao jobDao;

    @Autowired
    private JobInstanceDao jobInstanceDao;

    @Autowired
    private JobInstanceShardDao jobInstanceShardDao;

    private final ExecutorService executor;


    private final Retryer<Boolean> checkJobInstanceFinishRetryer = Retryers.get().newRetryer(Predicates.<Boolean>alwaysFalse(), 5);

    public JobSupport(){
        executor = Executors.newExecutor(Systems.cpuNum(), 10000, "JOB-FINISH-CHECKER-");
    }


    public void triggerJobInstance(String appName, String jobClass, JobInstance instance) {
        String jobInstancePath = ZkPaths.pathOfJobInstance(appName, jobClass, instance.getId());
        zk.client().create(jobInstancePath, instance.getStatus());
    }


    public JobInstanceWaitResp waitingJobInstanceFinish(final String appName, final String jobClass, final Long jobInstanceId, long timeout) {

        final CountDownLatch latch = new CountDownLatch(1);

        String jobInstanceNode = ZkPaths.pathOfJobInstance(appName, jobClass, jobInstanceId);

        NodeWatcher watcher = zk.client().newNodeWatcher(jobInstanceNode, new NodeListener() {
            @Override
            public void onDelete() {

                latch.countDown();
            }
        });

        try {

            Logs.info("Waiting the job({}/{}/{}) with timeout({}) to be finished.", appName, jobClass, jobInstanceId, timeout);

            if (timeout > 0L){

                if (!latch.await(timeout, TimeUnit.SECONDS)){
                    return JobInstanceWaitResp.timeout();
                }
            } else {

                latch.await();
            }

        } catch (InterruptedException e) {

            throw new RuntimeException(e);
        } finally {
            if (watcher != null){
                watcher.stop();
            }
        }

        Logs.info("The job({}/{}/{}) has finished.", appName, jobClass, jobInstanceId);

        return JobInstanceWaitResp.success();
    }



    public Boolean deleteJobInstance(final String appName, final String jobClass, final JobInstance instance){
        return deleteJobInstance(appName, jobClass, instance.getId());
    }


    public Boolean deleteJobInstance(final String appName, final String jobClass, final Long jobInstanceId){


        String jobInstanceNode = ZkPaths.pathOfJobInstance(appName, jobClass, jobInstanceId);
        zk.client().deleteIfExists(jobInstanceNode);

        return Boolean.TRUE;
    }


    public Boolean deleteJobInstances(String appName, String jobClass) {

        List<String> instanceIds = findJobInstances(appName, jobClass);
        if (!CollectionUtil.isNullOrEmpty(instanceIds)){
            for (String instanceId : instanceIds){
                deleteJobInstance(appName, jobClass, Long.valueOf(instanceId));
            }
        }

        return Boolean.TRUE;
    }


    public List<String> findJobInstances(String appName, String jobClass){
        String jobInstancesNode = ZkPaths.pathOfJobInstances(appName, jobClass);
        return zk.client().gets(jobInstancesNode);
    }


    public Boolean updateJobFireTime(String appName, String jobClass, JobFireTime jobFireTime) {
        String jobFireTimeNode = ZkPaths.pathOfJobFireTime(appName, jobClass);
        zk.client().mkdirs(jobFireTimeNode);
        return zk.client().update(jobFireTimeNode, JSON.toJSONString(jobFireTime));
    }


    public JobFireTime getJobFireTime(String appName, String jobClass){
        String jobFireTimeNode = ZkPaths.pathOfJobFireTime(appName, jobClass);
        if (!zk.client().checkExists(jobFireTimeNode)){
            return null;
        }
        return zk.client().getJson(jobFireTimeNode, JobFireTime.class);
    }


    public Boolean updateJobStateDirectly(String appName, String jobClass, JobState state){
        String jobStateNode = ZkPaths.pathOfJobState(appName, jobClass);
        zk.client().mkdirs(jobStateNode);
        return zk.client().update(jobStateNode, state.value());
    }


    public Boolean updateJobStateSafely(String appName, String jobClass, JobState targetState){

        JobState currentState = getJobState(appName, jobClass);
        if(!SimpleJobStateMachine.get().allow(currentState, targetState)){
            throw new JobStateTransferInvalidException(appName + "/" + jobClass, currentState, targetState);
        }

        String jobStateNode = ZkPaths.pathOfJobState(appName, jobClass);
        return zk.client().update(jobStateNode, targetState.value());
    }


    public void checkJobStateOperate(String appName, String jobClass, JobState expectState, JobState targetState){
        JobState currentState = getJobState(appName, jobClass);
        if ((expectState != null && expectState != currentState)
                || !SimpleJobStateMachine.get().allow(currentState, targetState)){
            throw new JobStateTransferInvalidException(appName + "/" + jobClass, currentState, targetState);
        }
    }


    public JobState getJobState(String appName, String jobClass) {
        String jobStateNode = ZkPaths.pathOfJobState(appName, jobClass);
        if (!zk.client().checkExists(jobStateNode)){
            return JobState.STOPPED;
        }
        return JobState.from(zk.client().getInteger(jobStateNode));
    }


    public Boolean updateJobScheduler(String appName, String jobClass, String scheduler) {
        String jobSchedulerNode = ZkPaths.pathOfJobScheduler(appName, jobClass);
        zk.client().mkdirs(jobSchedulerNode);
        return zk.client().update(jobSchedulerNode, scheduler);
    }


    public String getJobScheduler(String appName, String jobClass) {
        String jobSchedulerNode = ZkPaths.pathOfJobScheduler(appName, jobClass);
        if (!zk.client().checkExists(jobSchedulerNode)){
            return null;
        }
        return zk.client().getString(jobSchedulerNode);
    }


    public Boolean mkJobInstances(String appName, String jobClass) {
        return zk.client().mkdirs(ZkPaths.pathOfJobInstances(appName, jobClass));
    }


    public Boolean removeJob(JobDetail jobDetail){
        String appJobPath = ZkPaths.pathOfJob(jobDetail.getApp().getAppName(), jobDetail.getJob().getClazz());
        zk.client().deleteRecursivelyIfExists(appJobPath);
        return Boolean.TRUE;
    }


    public Boolean checkJobScheduling(String appName, String jobClass) {

        String jobPath = ZkPaths.pathOfJob(appName, jobClass);
        if(!zk.client().checkExists(jobPath)){
            return Boolean.FALSE;
        }

        String scheduler = getJobScheduler(appName, jobClass);
        if(Strings.isNullOrEmpty(scheduler)){

            return Boolean.FALSE;
        }

        if(!zk.client().checkExists(ZkPaths.pathOfServer(scheduler))){

            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }


    public void checkJobInstanceFinish(final ShardFinishDto shardFinishDto){
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    checkJobInstanceFinishRetryer.call(new RetryableCheckJobInstanceFinishTask(shardFinishDto));

                } catch (Exception e) {
                    Logs.error("failed to check job instance finish({}), cause: {}", shardFinishDto, Throwables.getStackTraceAsString(e));
                }
            }
        });
    }


    public boolean hasJobInstance(String appName, String jobClass) {

        String jobInstanceNodePath = ZkPaths.pathOfJobInstances(appName, jobClass);

        List<String> instances = zk.client().gets(jobInstanceNodePath);

        return !CollectionUtil.isNullOrEmpty(instances);
    }


    public Boolean forceStopJobInstance(JobDetail jobDetail, JobInstanceStatus finalStatus) {

        List<String> jobInstanceIds = findJobInstances(jobDetail.getApp().getAppName(), jobDetail.getJob().getClazz());
        if (!CollectionUtil.isNullOrEmpty(jobInstanceIds)){
            for (String jobInstanceId : jobInstanceIds){
                forceStopJobInstance(jobDetail, Long.valueOf(jobInstanceId), finalStatus);
            }
        }

        return Boolean.TRUE;
    }


    private void forceStopJobInstance(JobDetail jobDetail, Long jobInstanceId, JobInstanceStatus finalStatus) {



        Lock jobInstanceLock = lockJobInstance(jobInstanceId);
        while (!jobInstanceLock.lock(5000)){

            Logs.warn("failed to lock the job instance when force stop job instance(jobDetail={}, jobInstanceId={}).", jobDetail, jobInstanceId);
        }

        try {

            JobInstance instance = jobInstanceDao.findById(jobInstanceId);
            if (JobInstanceStatus.isFinal(instance.getStatus())){

                return;
            }


            String appName = jobDetail.getApp().getAppName();
            String jobClass = jobDetail.getJob().getClazz();
            if(!deleteJobInstance(appName, jobClass, instance)){
                Logs.warn("failed to delete job instance from zk when force stop job instance((jobDetail={}, jobInstance={})).", instance, jobDetail);
            }

            instance.setUtime(new Date());
            instance.setStatus(finalStatus.value());
            jobInstanceDao.save(instance);

            updateJobStateSafely(appName, jobClass, JobState.WAITING);

        } catch (Exception e){
            Logs.error("failed to force stop job instance(jobDetail={}, jobInstanceId={}), cause: {}",
                    jobDetail, jobInstanceId, Throwables.getStackTraceAsString(e));
        } finally {
            jobInstanceLock.unlock();
        }
    }


    private class RetryableCheckJobInstanceFinishTask implements Callable<Boolean> {

        private final ShardFinishDto shardFinishDto;

        public RetryableCheckJobInstanceFinishTask(ShardFinishDto shardFinishDto) {
            this.shardFinishDto = shardFinishDto;
        }

        @Override
        public Boolean call() throws Exception {
            return doCheckJobInstanceFinish(shardFinishDto);
        }
    }


    private Boolean doCheckJobInstanceFinish(ShardFinishDto shardFinishDto) {

        Long instanceId = shardFinishDto.getInstanceId();



        Lock jobInstanceLock = lockJobInstance(instanceId);
        while (!jobInstanceLock.lock(5000)){

            Logs.warn("failed to lock the job instance(id={}) when check job instance finish, will retry", instanceId);
        }


        try {

            JobInstance instance = jobInstanceDao.findById(instanceId);
            if (JobInstanceStatus.isFinal(instance.getStatus())){

                return Boolean.TRUE;
            }


            Integer totalShardCount = jobInstanceShardDao.getJobInstanceTotalShardCount(instanceId);
            Integer successShardCount = jobInstanceShardDao.getJobInstanceStatusShardCount(instanceId, JobInstanceShardStatus.SUCCESS);
            Integer failedShardCount = jobInstanceShardDao.getJobInstanceStatusShardCount(instanceId, JobInstanceShardStatus.FAILED);

            if (Objects.equal(totalShardCount, successShardCount + failedShardCount)){


                Job job = jobDao.findById(instance.getJobId());
                App app = appDao.findById(job.getAppId());
                if(!deleteJobInstance(app.getAppName(), job.getClazz(), instance)){
                    Logs.warn("failed to delete job instance({}) from zk when check the job instance finish.", instance);
                }


                instance.setEndTime(shardFinishDto.getEndTime());
                if (failedShardCount > 0){

                    instance.setStatus(JobInstanceStatus.FAILED.value());
                } else {

                    instance.setStatus(JobInstanceStatus.SUCCESS.value());
                }
                instance.setUtime(new Date());

                return jobInstanceDao.save(instance);
            }

            return Boolean.TRUE;
        } catch (Exception e){
            Logs.error("failed to check whether the job instance(id={}) has finished, cause: {}",
                    instanceId, Throwables.getStackTraceAsString(e));
            return Boolean.FALSE;
        } finally {
            jobInstanceLock.unlock();
        }
    }


    private Lock lockJobInstance(Long jobInstanceId){
        return zk.client().newLock(ZkPaths.pathOfJobInstanceLock(jobInstanceId));
    }

    @Override
    public void destroy() throws Exception {
        executor.shutdown();
    }
}
