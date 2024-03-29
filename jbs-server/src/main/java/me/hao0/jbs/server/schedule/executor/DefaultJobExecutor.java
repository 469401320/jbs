package me.hao0.jbs.server.schedule.executor;

import com.google.common.base.Throwables;
import me.hao0.jbs.common.dto.JobDetail;
import me.hao0.jbs.common.dto.JobFireTime;
import me.hao0.jbs.common.dto.JobInstanceWaitResp;
import me.hao0.jbs.common.exception.JobStateTransferInvalidException;
import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.model.JobInstance;
import me.hao0.jbs.common.model.enums.JobInstanceStatus;
import me.hao0.jbs.common.model.enums.JobState;
import me.hao0.jbs.common.model.enums.JobTriggerType;
import me.hao0.jbs.common.util.*;
import me.hao0.jbs.server.cluster.client.ClientCluster;
import me.hao0.jbs.server.cluster.server.ServerHost;
import me.hao0.jbs.server.event.core.EventDispatcher;
import me.hao0.jbs.server.event.job.JobFailedEvent;
import me.hao0.jbs.server.event.job.JobFinishedEvent;
import me.hao0.jbs.server.event.job.JobTimeoutEvent;
import me.hao0.jbs.server.exception.JobInstanceCreateException;
import me.hao0.jbs.store.util.Dates;
import me.hao0.jbs.store.service.JobService;
import me.hao0.jbs.store.support.JobSupport;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;


@Component
public class DefaultJobExecutor implements JobExecutor {

    @Autowired
    private JobService jobService;

    @Autowired
    private ServerHost serverHost;

    @Autowired
    private ClientCluster clientCluster;

    @Autowired
    private JobSupport jobSupport;

    @Autowired
    private EventDispatcher eventDispatcher;

    private final ExecutorService asyncExecutor =
            Executors.newExecutor(Systems.cpuNum(), 10000, "DEFAULT-JOB-ASYNC-EXECUTOR-");

    @Override
    public void execute(final JobDetail jobDetail, JobTriggerType triggerType, JobExecutionContext context) {

        final String appName = jobDetail.getApp().getAppName();
        final String jobClass = jobDetail.getJob().getClazz();

        JobInstance instance = null;
        try {

            Logs.info("The job({}/{}) is fired.", appName, jobClass);

            if (triggerType == JobTriggerType.DEFAULT){

                asyncExecutor.submit(new RefreshJobFireTimeTask(appName, jobClass, context));
            }

            if (!canRunJobInstance(appName, jobClass, jobDetail.getJob().getId())){
                return;
            }


            jobSupport.updateJobStateDirectly(appName, jobClass, JobState.RUNNING);


            instance = createInstanceAndShards(jobDetail, triggerType);


            jobSupport.triggerJobInstance(appName, jobClass, instance);


            Long timeout = jobDetail.getConfig().getTimeout();
            timeout = timeout == null ? 0L : timeout;
            JobInstanceWaitResp finishResp = jobSupport.waitingJobInstanceFinish(appName, jobClass, instance.getId(), timeout);
            if (finishResp.isSuccess()){


                eventDispatcher.publish(new JobFinishedEvent(instance.getJobId(), instance.getId()));
            } else if (finishResp.isTimeout()){

                eventDispatcher.publish(new JobTimeoutEvent(instance.getJobId(), instance.getId(), buildJobTimeoutDetail(instance)));
            }


            jobSupport.updateJobStateSafely(appName, jobClass, JobState.WAITING);


        } catch (JobStateTransferInvalidException e){

            Logs.warn("failed to update job state(instances={}), cause: {}.", instance, e.toString());
        } catch (JobInstanceCreateException e){

            String cause = Throwables.getStackTraceAsString(e);
            Logs.error("failed to create job instance when execute job(jobDetail={}, instance={}), cause: {}",
                    jobDetail, instance, cause);
            handleJobExecuteFailed(jobDetail, instance, appName, jobClass, cause);
        } catch (Exception e){

            String cause = Throwables.getStackTraceAsString(e);
            Logs.error("failed to execute job(jobDetail={}, instance={}), cause: {}",
                    jobDetail, instance, cause);
            handleJobExecuteFailed(jobDetail, instance, appName, jobClass, cause);
        }
    }

    private String buildJobTimeoutDetail(JobInstance instance) {
        return "startTime：" + Dates.format(instance.getStartTime()) + ", " +
               "elapsedTime：" + Dates.timeIntervalStr(instance.getStartTime(), new Date());
    }

    private boolean canRunJobInstance(String appName, String jobClass, Long jobId) {

        if (!hasAvailableClients(appName, jobId)){


            jobSupport.deleteJobInstances(appName, jobClass);


            Logs.warn("Invalid job({}/{}) fired, because there are no available clients.", appName, jobClass);
            return Boolean.FALSE;
        }

        if (jobSupport.hasJobInstance(appName, jobClass)){
            Logs.warn("The job({}/{}) has a running instance, so ignore this execution.", appName, jobClass);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    private boolean hasAvailableClients(String appName, Long jobId){

        List<String> clients = clientCluster.getAliveClients(appName);
        if (CollectionUtil.isNullOrEmpty(clients)){
            return false;
        }


        Response<Set<String>> assignsResp = jobService.listSimpleJobAssigns(jobId);
        if (!assignsResp.isSuccess()){
            return true;
        }

        Set<String> assigns = assignsResp.getData();
        if (!CollectionUtil.isNullOrEmpty(assigns)){
            for (String client : clients){
                if (assigns.contains(client.split(":")[0])){
                    return true;
                }
            }
            return false;
        }

        return true;
    }


    private void handleJobExecuteFailed(JobDetail jobDetail, JobInstance instance, String appName, String jobClass, String cause) {
        try {

            if (instance == null){
                return;
            }

            if (cause.length() > Constants.MAX_ERROR_LENGTH){
                cause = cause.substring(0, Constants.MAX_ERROR_LENGTH);
            }


            jobService.failedJobInstance(instance.getId(), cause);


            jobSupport.deleteJobInstance(appName, jobClass, instance);


            jobSupport.updateJobStateDirectly(appName, jobClass, JobState.WAITING);

        } catch (Exception e){
            Logs.error("failed to handle the job(instance={}, appName={}, jobClass={}) execute failed, cause: {}",
                    instance, appName, jobClass, Throwables.getStackTraceAsString(e));
        } finally {


            Long instanceId = instance == null ? null : instance.getId();
            eventDispatcher.publish(new JobFailedEvent(jobDetail.getJob().getId(), instanceId, cause));
        }
    }


    private JobInstance createInstanceAndShards(JobDetail detail, JobTriggerType triggerType) {

        JobInstance instance = new JobInstance();
        instance.setJobId(detail.getJob().getId());
        instance.setStatus(JobInstanceStatus.RUNNING.value());
        instance.setTriggerType(triggerType.value());
        instance.setServer(serverHost.get());
        instance.setStartTime(new Date());

        Response<Boolean> saveResp = jobService.createJobInstanceAndShards(instance, detail.getConfig());
        if (!saveResp.isSuccess() || !saveResp.getData()){
            throw new JobInstanceCreateException(saveResp.getErr().toString());
        }

        return instance;
    }

    private class RefreshJobFireTimeTask implements Runnable {

        private final String appName;

        private final String jobClass;

        private final JobExecutionContext context;

        public RefreshJobFireTimeTask(String appName, String jobClass, JobExecutionContext context) {
            this.appName = appName;
            this.jobClass = jobClass;
            this.context = context;
        }

        @Override
        public void run() {
            try {


                JobFireTime jobFireTime = new JobFireTime();

                jobFireTime.setCurrent(Dates.format(context.getFireTime()));
                jobFireTime.setPrev(Dates.format(context.getPreviousFireTime()));
                jobFireTime.setNext(Dates.format(context.getNextFireTime()));

                jobSupport.updateJobFireTime(appName, jobClass, jobFireTime);

            } catch (Exception e){
                Logs.error("failed to execute async task when execute job({}/{}), cause: {}.",
                        appName, jobClass, Throwables.getStackTraceAsString(e));
            }
        }
    }
}
