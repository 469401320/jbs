package me.hao0.jbs.common.model;

import me.hao0.jbs.common.anno.RedisModel;
import java.util.Date;


@RedisModel(prefix = "job_cfgs")
public class JobConfig implements Model<Long> {

    private static final long serialVersionUID = 4800351890221647029L;


    private Long id;


    private Long jobId;


    private Boolean misfire;


    private String param;


    private Integer shardCount;


    private String shardParams;


    private Integer maxShardPullCount;


    private Long timeout;


    private Date ctime;


    private Date utime;

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

    public Boolean getMisfire() {
        return misfire;
    }

    public void setMisfire(Boolean misfire) {
        this.misfire = misfire;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Integer getShardCount() {
        return shardCount;
    }

    public void setShardCount(Integer shardCount) {
        this.shardCount = shardCount;
    }

    public String getShardParams() {
        return shardParams;
    }

    public void setShardParams(String shardParams) {
        this.shardParams = shardParams;
    }

    public Integer getMaxShardPullCount() {
        return maxShardPullCount;
    }

    public void setMaxShardPullCount(Integer maxShardPullCount) {
        this.maxShardPullCount = maxShardPullCount;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
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
        return "JobConfig{" +
                "id=" + id +
                ", jobId=" + jobId +
                ", misfire=" + misfire +
                ", param='" + param + '\'' +
                ", shardCount=" + shardCount +
                ", shardParams='" + shardParams + '\'' +
                ", maxShardPullCount=" + maxShardPullCount +
                ", timeout=" + timeout +
                ", ctime=" + ctime +
                ", utime=" + utime +
                '}';
    }
}
