package me.hao0.jbs.common.model;

import me.hao0.jbs.common.anno.RedisModel;
import me.hao0.jbs.common.model.enums.JobInstanceStatus;
import me.hao0.jbs.common.model.enums.JobTriggerType;

import java.util.Date;


@RedisModel(prefix = "job_inss")
public class JobInstance implements Model<Long> {

    private static final long serialVersionUID = -6691569994755828004L;


    private Long id;


    private Long jobId;


    private Integer status;


    private Integer triggerType;


    private String server;


    private Integer maxShardPullCount;


    private String jobParam;


    private Integer totalShardCount;


    private Date startTime;


    private Date endTime;


    private String cause;


    private Date ctime;


    private Date utime;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(Integer triggerType) {
        this.triggerType = triggerType;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getMaxShardPullCount() {
        return maxShardPullCount;
    }

    public void setMaxShardPullCount(Integer maxShardPullCount) {
        this.maxShardPullCount = maxShardPullCount;
    }

    public String getJobParam() {
        return jobParam;
    }

    public void setJobParam(String jobParam) {
        this.jobParam = jobParam;
    }

    public Integer getTotalShardCount() {
        return totalShardCount;
    }

    public void setTotalShardCount(Integer totalShardCount) {
        this.totalShardCount = totalShardCount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Date getCtime() {
        return ctime;
    }

    @Override
    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    @Override
    public void setUtime(Date utime) {
        this.utime = utime;
    }

    @Override
    public String toString() {
        return "JobInstance{" +
                "id=" + id +
                ", jobId=" + jobId +
                ", status=" + status +
                ", triggerType=" + triggerType +
                ", server='" + server + '\'' +
                ", maxShardPullCount=" + maxShardPullCount +
                ", jobParam='" + jobParam + '\'' +
                ", totalShardCount=" + totalShardCount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", cause='" + cause + '\'' +
                ", ctime=" + ctime +
                ", utime=" + utime +
                '}';
    }
}
