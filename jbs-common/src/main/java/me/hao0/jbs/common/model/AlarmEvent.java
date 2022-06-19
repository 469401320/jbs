package me.hao0.jbs.common.model;

import java.util.Date;


public class AlarmEvent implements Model<Long> {


    private Long id;


    private Long jobId;


    private Integer type;


    private String detail;


    private Date ctime;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    @Override
    public String toString() {
        return "AlarmEvent{" +
                "id=" + id +
                ", jobId=" + jobId +
                ", type=" + type +
                ", detail='" + detail + '\'' +
                ", ctime=" + ctime +
                '}';
    }
}
