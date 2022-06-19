package me.hao0.jbs.common.model;

import me.hao0.jbs.common.anno.RedisModel;

import java.io.Serializable;


@RedisModel(prefix = "job_srs")
public class JobServer implements Serializable {

    private static final long serialVersionUID = -5081467017356824898L;

    private Long jobId;

    private String server;

    public JobServer(){}

    public JobServer(Long jobId, String server) {
        this.jobId = jobId;
        this.server = server;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return "JobServer{" +
                "jobId=" + jobId +
                ", server='" + server + '\'' +
                '}';
    }
}
