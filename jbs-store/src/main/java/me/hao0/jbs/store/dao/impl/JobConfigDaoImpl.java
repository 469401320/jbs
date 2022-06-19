package me.hao0.jbs.store.dao.impl;

import com.google.common.base.Strings;
import me.hao0.jbs.common.model.JobConfig;
import me.hao0.jbs.store.dao.JobConfigDao;
import me.hao0.jbs.store.support.RedisKeys;
import org.springframework.stereotype.Repository;


@Repository
public class JobConfigDaoImpl extends RedisDao<JobConfig> implements JobConfigDao {

    @Override
    public Boolean bindJob(Long jobId, Long jobConfigId) {
        String jobConfigMappingsKey = RedisKeys.JOB_CONFIG_MAPPINGS;
        redis.opsForHash().put(jobConfigMappingsKey, jobId.toString(), jobConfigId.toString());
        return Boolean.TRUE;
    }

    @Override
    public Boolean unbindJob(Long jobId, Long jobConfigId) {
        String jobConfigMappingsKey = RedisKeys.JOB_CONFIG_MAPPINGS;
        redis.opsForHash().delete(jobConfigMappingsKey, jobId.toString());
        return Boolean.TRUE;
    }

    @Override
    public JobConfig findByJobId(Long jobId) {
        String jobConfigMappingsKey = RedisKeys.JOB_CONFIG_MAPPINGS;
        String configId = String.valueOf(redis.opsForHash().get(jobConfigMappingsKey, jobId.toString()));
        if (Strings.isNullOrEmpty(configId)){
            return null;
        }
        return findById(Long.valueOf(configId));
    }
}
