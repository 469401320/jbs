package me.hao0.jbs.common.dto;

import me.hao0.jbs.common.model.enums.JobState;
import java.io.Serializable;


public class JobControl implements Serializable {

    private static final long serialVersionUID = 8521933124536616448L;


    private Long id;


    private String clazz;


    private String cron;


    private String desc;


    private String scheduler;


    private String fireTime;


    private String prevFireTime;


    private String nextFireTime;


    private Integer state;


    private String stateDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public String getFireTime() {
        return fireTime;
    }

    public void setFireTime(String fireTime) {
        this.fireTime = fireTime;
    }

    public String getPrevFireTime() {
        return prevFireTime;
    }

    public void setPrevFireTime(String prevFireTime) {
        this.prevFireTime = prevFireTime;
    }

    public String getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(String nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setStateAndDesc(JobState state){
        setState(state.value());
        setStateDesc(state.code());
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    @Override
    public String toString() {
        return "JobControl{" +
                "id=" + id +
                ", clazz='" + clazz + '\'' +
                ", cron='" + cron + '\'' +
                ", desc='" + desc + '\'' +
                ", scheduler='" + scheduler + '\'' +
                ", fireTime='" + fireTime + '\'' +
                ", prevFireTime='" + prevFireTime + '\'' +
                ", nextFireTime='" + nextFireTime + '\'' +
                ", state=" + state +
                ", stateDesc='" + stateDesc + '\'' +
                '}';
    }
}
