package me.hao0.jbs.server.event.job;

import me.hao0.jbs.server.event.core.Event;


public abstract class JobEvent implements Event {

    protected Long jobId;

    public JobEvent(Long jobId) {
        this.jobId = jobId;
    }

    public Long getJobId() {
        return jobId;
    }

    @Override
    public String toString() {
        return "JobEvent{" +
                "jobId=" + jobId +
                '}';
    }
}
