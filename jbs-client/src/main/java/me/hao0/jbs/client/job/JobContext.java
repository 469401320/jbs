package me.hao0.jbs.client.job;


public interface JobContext {


    Long getInstanceId();

    void setInstanceId(Long instanceId);


    String getJobParam();

    void setJobParam(String jobParam);


    Long getShardId();

    void setShardId(Long shardId);


    Integer getShardItem();

    void setShardItem(Integer shardItem);


    String getShardParam();

    void setShardParam(String shardParam);


    Integer getTotalShardCount();

    void setTotalShardCount(Integer totalShardCount);
}
