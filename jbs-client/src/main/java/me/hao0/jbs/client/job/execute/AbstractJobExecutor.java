package me.hao0.jbs.client.job.execute;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import me.hao0.jbs.client.core.JbsClient;
import me.hao0.jbs.client.job.DefaultJob;
import me.hao0.jbs.client.job.JobContext;
import me.hao0.jbs.client.job.JobContextImpl;
import me.hao0.jbs.client.job.JobResult;
import me.hao0.jbs.client.job.listener.JobListener;
import me.hao0.jbs.client.job.listener.JobResultListener;
import me.hao0.jbs.client.job.script.DefaultScriptExecutor;
import me.hao0.jbs.client.job.script.ScriptExecutor;
import me.hao0.jbs.client.job.script.ScriptJob;
import me.hao0.jbs.client.job.Job;
import me.hao0.jbs.common.dto.PullShard;
import me.hao0.jbs.common.dto.ShardFinishDto;
import me.hao0.jbs.common.model.enums.ShardOperateRespCode;
import me.hao0.jbs.common.support.Component;
import static me.hao0.jbs.common.util.Constants.*;
import me.hao0.jbs.common.util.Executors;
import me.hao0.jbs.common.util.Systems;
import me.hao0.jbs.common.util.ZkPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;


public abstract class AbstractJobExecutor extends Component implements JobExecutor {

    private final Logger log = LoggerFactory.getLogger(AbstractJobExecutor.class);

    protected final JbsClient client;

    private ExecutorService executor;


    private final ScriptExecutor scriptExecutor = new DefaultScriptExecutor();

    public AbstractJobExecutor(JbsClient client) {
        this.client = client;
    }

    @Override
    public void doStart() {
       executor = Executors.newExecutor(client.getExecutorThreadCount(), 10000, "JOB-EXECUTOR-");
    }

    @Override
    public void doShutdown() {
        executor.shutdown();
    }

    public void execute(final Long instanceId, final ZkJob zkJob) {
        executor.submit(new ExecuteShardTask(instanceId, zkJob));
    }


    private class ExecuteShardTask implements Runnable{

        private final Long instanceId;

        private final ZkJob zkJob;

        public ExecuteShardTask(Long instanceId, ZkJob zkJob) {
            this.instanceId = instanceId;
            this.zkJob = zkJob;
        }

        @Override
        public void run() {
            try {

                PullShard oneShard;
                for(;;){


                    oneShard = pullShard(instanceId, zkJob);
                    if (oneShard == null){

                        break;
                    }

                    doExecuteShard(instanceId, zkJob, oneShard);


                }

            } catch (Exception e){
                log.error("failed to execute shard task(job={}, instanceId={}), cause: {}",
                        zkJob.getJob(), instanceId, Throwables.getStackTraceAsString(e));
            }
        }
    }

    private void doExecuteShard(Long instanceId, ZkJob zkJob, PullShard oneShard) {


        JobContext context = buildJobContext(instanceId, oneShard);


        Job job = zkJob.getJob();


        if (job instanceof JobListener){
            ((JobListener) job).onBefore(context);
        }

        Date startTime = new Date();

        JobResult res = null;
        if (job instanceof DefaultJob){

            res = job.execute(context);
        } else if (job instanceof ScriptJob){

            res = executeScript(context);
        }

        Date endTime = new Date();


        if (job instanceof JobListener){
            ((JobListener) job).onAfter(context, res);
        }


        if (res == null || res.is(JobResult.SUCCESS)){



            ShardFinishDto shardFinishDto = buildShardFinishDto(instanceId, context.getShardId(), startTime, endTime);
            shardFinishDto.setSuccess(Boolean.TRUE);
            finishShard(shardFinishDto, zkJob);


            if (job instanceof JobResultListener){
                ((JobResultListener)job).onSuccess();
            }

        } else if(res.is(JobResult.FAIL)){


            ShardFinishDto shardFinishDto = buildShardFinishDto(instanceId, context.getShardId(), startTime, endTime);
            shardFinishDto.setSuccess(Boolean.FALSE);
            shardFinishDto.setCause(res.getError());
            finishShard(shardFinishDto, zkJob);


            if (job instanceof JobResultListener){
                ((JobResultListener)job).onFail();
            }

        } else if(res.is(JobResult.LATER)){


            returnShard(instanceId, context.getShardId(), zkJob);
        }
    }

    private JobResult executeScript(JobContext context) {

        String cmd = context.getJobParam();

        Map<String, String> env = Maps.newHashMapWithExpectedSize(2);
        env.put(SCRIPT_JOB_ENV_SHARD_ITEM, context.getShardId() + "");
        if (Strings.isNullOrEmpty(context.getShardParam())){
            env.put(SCRIPT_JOB_ENV_SHARD_PARAM, context.getShardParam());
        }

        return scriptExecutor.exec(cmd, env);
    }

    private JobContext buildJobContext(Long instanceId, PullShard shard) {

        JobContext context = new JobContextImpl();

        context.setInstanceId(instanceId);
        context.setShardId(shard.getId());
        context.setShardParam(shard.getParam());
        context.setShardItem(shard.getItem());
        context.setJobParam(shard.getJobParam());
        context.setTotalShardCount(shard.getTotalShardCount());

        return context;
    }

    private ShardFinishDto buildShardFinishDto(Long instanceId, Long shardId, Date startTime, Date endTime) {

        ShardFinishDto shardFinishDto = new ShardFinishDto();

        shardFinishDto.setInstanceId(instanceId);
        shardFinishDto.setShardId(shardId);
        shardFinishDto.setClient(Systems.hostPid());
        shardFinishDto.setStartTime(startTime);
        shardFinishDto.setEndTime(endTime);

        return shardFinishDto;
    }

    protected void checkInvalidInstance(Long instanceId, ZkJob zkJob, ShardOperateRespCode code) {
        if (code != null){

            if (ShardOperateRespCode.needCleanJobInstance(code)){

                String jobInstancePath = ZkPaths.pathOfJobInstance(client.getAppName(), zkJob.getJobClass(), instanceId);
                client.getZk().deleteIfExists(jobInstancePath);
            }
        }
    }


    protected abstract PullShard pullShard(Long instanceId, ZkJob zkJob);


    protected abstract Boolean returnShard(Long instanceId, Long shardId, ZkJob zkJob);


    protected abstract Boolean finishShard(ShardFinishDto shardFinishDto, ZkJob zkJob);
}
