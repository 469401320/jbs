package me.hao0.jbs.common.dto;

import java.io.Serializable;


public class PullShard implements Serializable {

    private static final long serialVersionUID = -7899746031432616077L;


    private Long id;


    private Integer item;


    private String param;


    private String jobParam;


    private Integer totalShardCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "PullShard{" +
                "id=" + id +
                ", item=" + item +
                ", param='" + param + '\'' +
                ", jobParam='" + jobParam + '\'' +
                ", totalShardCount=" + totalShardCount +
                '}';
    }
}
