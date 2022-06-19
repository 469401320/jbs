package me.hao0.jbs.client.job.script;

import me.hao0.jbs.client.job.JobResult;
import me.hao0.jbs.client.job.Job;
import me.hao0.jbs.client.job.JobContext;


public abstract class ScriptJob implements Job {

    @Override
    public JobResult execute(JobContext context) {
        return null;
    }
}
