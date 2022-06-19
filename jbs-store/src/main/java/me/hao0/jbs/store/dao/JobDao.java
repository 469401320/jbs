package me.hao0.jbs.store.dao;

import me.hao0.jbs.common.model.Job;
import java.util.List;


public interface JobDao extends BaseDao<Job> {


    Boolean bindApp(Long appId, Long jobId);


    Boolean unbindApp(Long appId, Long jobId);


    Boolean indexJobClass(Long appId, Long jobId, String clazz);


    Job findByJobClass(Long appId, String clazz);


    Long findIdByJobClass(Long appId, String clazz);


    Boolean unIndexJobClass(Long appId, String clazz);


    Long countByAppId(Long appId);


    List<Job> listByAppId(Long appId, Integer offset, Integer limit);
}
