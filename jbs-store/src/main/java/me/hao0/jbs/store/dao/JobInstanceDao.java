package me.hao0.jbs.store.dao;

import me.hao0.jbs.common.model.JobInstance;
import java.util.List;


public interface JobInstanceDao extends BaseDao<JobInstance> {


    Boolean bindJob(Long jobId, Long jobInstanceId);


    Boolean unbindJob(Long jobId, Long jobInstanceId);


    Long countByJobId(Long jobId);


    List<JobInstance> listByJobId(Long jobId, Integer offset, Integer limit);


    Long findMaxId(Long jobId);
}
