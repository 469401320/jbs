package me.hao0.jbs.store.dao;

import me.hao0.jbs.common.model.JobDependence;
import me.hao0.jbs.store.util.Page;


public interface JobDependenceDao extends BaseDao<JobDependence> {


    Boolean addDependence(JobDependence dependence);


    Page<Long> pagingNextJobIds(Long jobId, Integer offset, Integer limit);


    Boolean deleteNextJobIds(Long jobId);


    Boolean deleteNextJobId(Long jobId, Long nextJobId);
}
