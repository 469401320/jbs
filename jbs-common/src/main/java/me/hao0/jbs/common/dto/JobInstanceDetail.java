package me.hao0.jbs.common.dto;

import java.io.Serializable;


public class JobInstanceDetail implements Serializable {

    private static final long serialVersionUID = -3208492213218789547L;


    private Long jobId;


    private Long instanceId;


    private Integer status;


    private String statusDesc;


    private String cause;


    private String startTime;


    private String endTime;


    private Integer totalShardCount;


    private Integer waitShardCount;


    private Integer runningShardCount;


    private Integer successShardCount;


    private Integer failedShardCount;


    private Integer finishPercent;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalShardCount() {
        return totalShardCount;
    }

    public void setTotalShardCount(Integer totalShardCount) {
        this.totalShardCount = totalShardCount;
    }

    public Integer getSuccessShardCount() {
        return successShardCount;
    }

    public void setSuccessShardCount(Integer successShardCount) {
        this.successShardCount = successShardCount;
    }

    public Integer getFailedShardCount() {
        return failedShardCount;
    }

    public void setFailedShardCount(Integer failedShardCount) {
        this.failedShardCount = failedShardCount;
    }

    public Integer getWaitShardCount() {
        return waitShardCount;
    }

    public void setWaitShardCount(Integer waitShardCount) {
        this.waitShardCount = waitShardCount;
    }

    public Integer getRunningShardCount() {
        return runningShardCount;
    }

    public void setRunningShardCount(Integer runningShardCount) {
        this.runningShardCount = runningShardCount;
    }

    public Integer getFinishPercent() {
        return finishPercent;
    }

    public void setFinishPercent(Integer finishPercent) {
        this.finishPercent = finishPercent;
    }

    @Override
    public String toString() {
        return "JobInstanceDetail{" +
                "jobId=" + jobId +
                ", instanceId=" + instanceId +
                ", status=" + status +
                ", statusDesc='" + statusDesc + '\'' +
                ", cause='" + cause + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", totalShardCount=" + totalShardCount +
                ", waitShardCount=" + waitShardCount +
                ", runningShardCount=" + runningShardCount +
                ", successShardCount=" + successShardCount +
                ", failedShardCount=" + failedShardCount +
                ", finishPercent=" + finishPercent +
                '}';
    }
}
