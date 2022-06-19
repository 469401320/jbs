package me.hao0.jbs.common.dto;

import me.hao0.jbs.common.model.enums.JobInstanceStatus;
import java.io.Serializable;


public class JobInstanceDto implements Serializable {

    private static final long serialVersionUID = 119889051080239175L;


    private Long id;


    private Long jobId;


    private Integer status;


    private String statusDesc;


    private Integer triggerType;


    private String triggerTypeDesc;


    private String server;


    private String startTime;


    private String endTime;


    private String costTime;


    private String cause;

    public Long getId() {
        return id;
    }

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

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public Integer getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(Integer triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerTypeDesc() {
        return triggerTypeDesc;
    }

    public void setTriggerTypeDesc(String triggerTypeDesc) {
        this.triggerTypeDesc = triggerTypeDesc;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
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

    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "JobInstanceDto{" +
                "id=" + id +
                ", jobId=" + jobId +
                ", status=" + status +
                ", statusDesc='" + statusDesc + '\'' +
                ", triggerType=" + triggerType +
                ", triggerTypeDesc='" + triggerTypeDesc + '\'' +
                ", server='" + server + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", costTime='" + costTime + '\'' +
                ", cause='" + cause + '\'' +
                '}';
    }
}
