package me.hao0.jbs.client.job.execute;

import me.hao0.jbs.client.job.Job;
import me.hao0.jbs.common.dto.ShardOperateResp;
import me.hao0.jbs.common.dto.ShardPullResp;
import me.hao0.jbs.common.retry.Retryer;


public class JobRetryer {


    private final Job job;

    private final Retryer<ShardPullResp> pullRetryer;

    private final Retryer<ShardOperateResp> finishRetryer;

    private final Retryer<ShardOperateResp> returnRetryer;

    public JobRetryer(Job job, Retryer<ShardPullResp> pullRetryer, Retryer<ShardOperateResp> finishRetryer, Retryer<ShardOperateResp> returnRetryer) {
        this.job = job;
        this.pullRetryer = pullRetryer;
        this.finishRetryer = finishRetryer;
        this.returnRetryer = returnRetryer;
    }

    public Job getJob() {
        return job;
    }

    public Retryer<ShardPullResp> getPullRetryer() {
        return pullRetryer;
    }

    public Retryer<ShardOperateResp> getFinishRetryer() {
        return finishRetryer;
    }

    public Retryer<ShardOperateResp> getReturnRetryer() {
        return returnRetryer;
    }
}
