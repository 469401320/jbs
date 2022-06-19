package me.hao0.jbs.store.dao;

import me.hao0.jbs.common.model.JobInstanceShard;
import me.hao0.jbs.common.model.enums.JobInstanceShardStatus;
import java.util.List;


public interface JobInstanceShardDao extends BaseDao<JobInstanceShard> {


    Boolean bindInstance(Long instanceId, Long progressId);


    Boolean unbindInstance(Long instanceId, Long progressId);


    Long countByInstanceId(Long instanceId);


    List<JobInstanceShard> listByInstanceId(Long instanceId, Integer offset, Integer limit);


    Boolean deleteByInstanceId(Long instanceId);


    Boolean createNewShardsSet(Long instanceId, List<Long> shardIds);


    Boolean returnShard2NewShardsSet(Long instanceId, Long shardId);


    Long pullShardFromNewShardsSet(Long instanceId);


    Boolean addShard2StatusSet(Long instanceId, Long shardId, JobInstanceShardStatus status);


    Boolean removeShardFromStatusSet(Long instanceId, Long shardId, JobInstanceShardStatus status);


    Boolean addShard2ClientRunningSet(String client, Long shardId);


    List<Long> getClientRunningShards(String client);


    Boolean removeShardFromClientRunningShards(String client, Long shardId);


    Integer getJobInstanceTotalShardCount(Long instanceId);


    Integer getJobInstanceStatusShardCount(Long instanceId, JobInstanceShardStatus status);


}
