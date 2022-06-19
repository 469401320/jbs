package me.hao0.jbs.store.dao.impl;

import me.hao0.jbs.common.model.JobInstance;
import me.hao0.jbs.store.dao.JobInstanceDao;
import me.hao0.jbs.store.support.RedisKeys;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public class JobInstanceDaoImpl extends RedisDao<JobInstance> implements JobInstanceDao {

    @Override
    public Boolean bindJob(Long jobId, Long jobInstanceId) {
        String jobInstancesKey = RedisKeys.keyOfJobInstances(jobId);
        return redis.opsForList()
                .leftPush(jobInstancesKey, jobInstanceId.toString()) > 0L;
    }

    @Override
    public Boolean unbindJob(Long jobId, Long jobInstanceId) {
        String jobInstancesKey = RedisKeys.keyOfJobInstances(jobId);
        return redis.opsForList()
                .remove(jobInstancesKey, 1, jobInstanceId.toString()) > 0L;
    }

    @Override
    public Long countByJobId(Long jobId) {
        return redis.opsForList().size(RedisKeys.keyOfJobInstances(jobId));
    }

    @Override
    public List<JobInstance> listByJobId(Long jobId, Integer offset, Integer limit) {
        return list(RedisKeys.keyOfJobInstances(jobId), offset, limit);
    }

    @Override
    public Long findMaxId(Long jobId) {
        return findMaxId(RedisKeys.keyOfJobInstances(jobId));
    }
}
