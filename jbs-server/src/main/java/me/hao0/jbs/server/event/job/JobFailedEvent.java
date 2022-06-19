package me.hao0.jbs.server.event.job;



public class JobFailedEvent extends JobEvent {

    private Long jobInstanceId;

    private String cause;

    public JobFailedEvent(Long jobId, Long jobInstanceId, String cause) {
        super(jobId);
        this.jobInstanceId = jobInstanceId;
        this.cause = cause;
    }

    public Long getJobInstanceId() {
        return jobInstanceId;
    }

    public String getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return "JobFailedEvent{" +
                "jobInstanceId=" + jobInstanceId +
                ", cause='" + cause + '\'' +
                "} " + super.toString();
    }
}
