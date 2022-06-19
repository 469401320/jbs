package me.hao0.jbs.server.event.job;



public class JobTimeoutEvent extends JobEvent {

    private Long jobInstanceId;

    private String detail;

    public JobTimeoutEvent(Long jobId, Long jobInstanceId, String detail) {
        super(jobId);
        this.jobInstanceId = jobInstanceId;
        this.detail = detail;
    }

    public Long getJobInstanceId() {
        return jobInstanceId;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public String toString() {
        return "JobTimeoutEvent{" +
                "jobInstanceId=" + jobInstanceId +
                "} " + super.toString();
    }
}
