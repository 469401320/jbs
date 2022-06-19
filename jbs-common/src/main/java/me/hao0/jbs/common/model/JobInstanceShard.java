package me.hao0.jbs.common.model;

import me.hao0.jbs.common.anno.RedisModel;
import java.util.Date;


@RedisModel(prefix = "job_ins_sds")
public class JobInstanceShard implements Model<Long> {

    private static final long serialVersionUID = 4699655089712303564L;


    private Long id;


    private Long instanceId;


    private Integer item;


    private String param;


    private String pullClient;


    private String finishClient;


    private Integer status;


    private String cause;


    private Date pullTime;


    private Integer pullCount;


    private Date startTime;


    private Date endTime;

    private Date ctime;

    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
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

    public Date getCtime() {
        return ctime;
    }

    @Override
    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    @Override
    public void setUtime(Date utime) {

    }

    public Date getPullTime() {
        return pullTime;
    }

    public void setPullTime(Date pullTime) {
        this.pullTime = pullTime;
    }

    public Integer getPullCount() {
        return pullCount;
    }

    public void setPullCount(Integer pullCount) {
        this.pullCount = pullCount;
    }

    @Override
    public String toString() {
        return "JobInstanceShard{" +
                "id=" + id +
                ", instanceId=" + instanceId +
                ", item=" + item +
                ", param='" + param + '\'' +
                ", pullClient='" + pullClient + '\'' +
                ", finishClient='" + finishClient + '\'' +
                ", status=" + status +
                ", cause='" + cause + '\'' +
                ", pullTime=" + pullTime +
                ", pullCount=" + pullCount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", ctime=" + ctime +
                '}';
    }
}
