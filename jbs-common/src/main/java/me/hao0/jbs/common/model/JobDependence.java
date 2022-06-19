package me.hao0.jbs.common.model;

import java.util.Date;


public class JobDependence implements Model<Long> {

    private static final long serialVersionUID = 4771124996803116397L;


    private Long jobId;


    private Long nextJobId;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getNextJobId() {
        return nextJobId;
    }

    public void setNextJobId(Long nextJobId) {
        this.nextJobId = nextJobId;
    }

    @Override
    public String toString() {
        return "JobDependence{" +
                "jobId=" + jobId +
                ", nextJobId=" + nextJobId +
                '}';
    }

    @Override
    public Long getId() {return 0L;}

    @Override
    public void setId(Long id) {}

    @Override
    public void setCtime(Date ctime) {}

    @Override
    public void setUtime(Date utime) {}
}
