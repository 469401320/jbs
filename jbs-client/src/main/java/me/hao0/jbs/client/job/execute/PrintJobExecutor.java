package me.hao0.jbs.client.job.execute;

import me.hao0.jbs.client.core.JbsClient;
import me.hao0.jbs.common.dto.PullShard;
import me.hao0.jbs.common.dto.ShardFinishDto;
import me.hao0.jbs.common.dto.ShardOperateResp;
import me.hao0.jbs.common.model.enums.ShardOperateRespCode;


public class PrintJobExecutor extends AbstractJobExecutor implements JobExecutor {

    public PrintJobExecutor(JbsClient client) {
        super(client);
    }

    @Override
    protected PullShard pullShard(Long instanceId, ZkJob zkJob) {

        System.out.println(zkJob.getJob().getClass().getName() + " is fired.");

        return null;
    }

    @Override
    protected Boolean returnShard(Long instanceId, Long shardId, ZkJob zkJob) {
        return null;
    }

    @Override
    protected Boolean finishShard(ShardFinishDto shardFinishDto, ZkJob zkJob) {

        ShardOperateResp finishResp = client.getHttp().finishJobInstanceShard(shardFinishDto);

        if (finishResp.getSuccess()){
            return Boolean.TRUE;
        }

        if (ShardOperateRespCode.needFinishAgain(finishResp.getCode())){

        }

        return Boolean.TRUE;
    }
}
