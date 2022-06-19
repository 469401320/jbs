package me.hao0.jbs.client.job.listener;

import me.hao0.jbs.client.job.JobResult;
import me.hao0.jbs.client.job.JobContext;


public interface JobListener {


    void onBefore(JobContext context);


    void onAfter(JobContext context, JobResult res);
}
