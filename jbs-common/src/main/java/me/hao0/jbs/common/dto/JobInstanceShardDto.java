package me.hao0.jbs.common.dto;

import java.io.Serializable;


public class JobInstanceShardDto implements Serializable {

    private static final long serialVersionUID = 4699655089712303564L;


    private Long id;


    private Long instanceId;


    private Integer item;


    private String param;


    private String pullClient;


    private String finishClient;


    private Integer status;


    private String statusDesc;


    private String cause;


    private Integer pullCount;


    private String pullTime;


    private String startTime;


    private String endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getPullClient() {
        return pullClient;
    }

    public void setPullClient(String pullClient) {
        this.pullClient = pullClient;
    }

    public String getFinishClient() {
        return finishClient;
    }

    public void setFinishClient(String finishClient) {
        this.finishClient = finishClient;
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

    public Integer getPullCount() {
        return pullCount;
    }

    public void setPullCount(Integer pullCount) {
        this.pullCount = pullCount;
    }

    public String getPullTime() {
        return pullTime;
    }

    public void setPullTime(String pullTime) {
        this.pullTime = pullTime;
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

    @Override
    public String toString() {
        return "JobInstanceShardDto{" +
                "id=" + id +
                ", instanceId=" + instanceId +
                ", item=" + item +
                ", param='" + param + '\'' +
                ", pullClient='" + pullClient + '\'' +
                ", finishClient='" + finishClient + '\'' +
                ", status=" + status +
                ", statusDesc='" + statusDesc + '\'' +
                ", pullTime=" + pullTime +
                ", pullCount=" + pullCount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
