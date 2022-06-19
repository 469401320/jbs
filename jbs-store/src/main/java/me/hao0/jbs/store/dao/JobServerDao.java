package me.hao0.jbs.store.dao;

import me.hao0.jbs.common.model.JobServer;
import java.util.List;


public interface JobServerDao {


    Boolean bind(JobServer jobServer);


    Boolean unbindJobsOfServer(String server);


    Boolean unbindJob(Long jobId);


    String findServerByJobId(Long jobId);


    List<Long> findJobsByServer(String server);


    Long countJobsByServer(String server);
}
