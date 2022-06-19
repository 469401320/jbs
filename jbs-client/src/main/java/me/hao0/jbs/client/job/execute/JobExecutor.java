package me.hao0.jbs.client.job.execute;

import me.hao0.jbs.common.support.Lifecycle;


public interface JobExecutor extends Lifecycle {


    void execute(Long instanceId, ZkJob zkJob);
}
