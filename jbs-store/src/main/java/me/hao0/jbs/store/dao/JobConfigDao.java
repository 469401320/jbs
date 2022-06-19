package me.hao0.jbs.store.dao;

import me.hao0.jbs.common.model.JobConfig;


public interface JobConfigDao extends BaseDao<JobConfig> {


    Boolean bindJob(Long jobId, Long jobConfigId);


    Boolean unbindJob(Long jobId, Long jobConfigId);


    JobConfig findByJobId(Long jobId);
}
