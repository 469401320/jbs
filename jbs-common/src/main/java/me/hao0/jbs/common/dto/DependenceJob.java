package me.hao0.jbs.common.dto;

import java.io.Serializable;


public class DependenceJob implements Serializable {

    private static final long serialVersionUID = -6905089976691794301L;


    private Long id;


    private String appName;


    private String jobClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    @Override
    public String toString() {
        return "DependenceJob{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", jobClass='" + jobClass + '\'' +
                '}';
    }
}
