package me.hao0.jbs.store.dao;

import me.hao0.jbs.common.model.JobAssign;
import java.util.Set;


public interface JobAssignDao extends BaseDao<JobAssign> {


    Boolean addAssign(Long jobId, String... clientIps);


    Boolean removeAssign(Long jobId, Object... clientIps);


    Boolean isAssigned(Long jobId, String ip);


    Set<String> listAssigns(Long jobId);


    Boolean cleanAssign(Long jobId);
}
