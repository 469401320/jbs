package me.hao0.jbs.common.dto;

import java.io.Serializable;
import java.util.Date;


public class ShardFinishDto implements Serializable {

    private static final long serialVersionUID = 7852332761131470264L;


    private String client;


    private Long instanceId;


    private Long shardId;


    private Date startTime;


    private Date endTime;


    private Boolean success = Boolean.TRUE;


    private String cause;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Long getShardId() {
        return shardId;
    }

    public void setShardId(Long shardId) {
        this.shardId = shardId;
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

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "ShardFinishDto{" +
                "client='" + client + '\'' +
                ", instanceId=" + instanceId +
                ", shardId=" + shardId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", success=" + success +
                ", cause='" + cause + '\'' +
                '}';
    }
}
