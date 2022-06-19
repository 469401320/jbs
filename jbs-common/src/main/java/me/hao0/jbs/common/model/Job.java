package me.hao0.jbs.common.model;

import me.hao0.jbs.common.model.enums.JobStatus;
import me.hao0.jbs.common.model.enums.JobType;

import java.util.Date;


public class Job implements Model<Long> {

    private static final long serialVersionUID = 6784880080835250983L;


    private Long id;


    private Long appId;


    private Integer type;


    private String clazz;


    private String cron;


    private Integer status;


    private String desc;


    private Date ctime;


    private Date utime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", appId=" + appId +
                ", type=" + type +
                ", clazz='" + clazz + '\'' +
                ", cron='" + cron + '\'' +
                ", status=" + status +
                ", desc='" + desc + '\'' +
                ", ctime=" + ctime +
                ", utime=" + utime +
                '}';
    }
}
