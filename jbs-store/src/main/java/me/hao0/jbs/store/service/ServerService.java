package me.hao0.jbs.store.service;

import me.hao0.jbs.common.util.Response;
import java.util.List;


public interface ServerService {


    Response<Boolean> scheduleJob(Long jobId);


    Response<Boolean> scheduleJobIfPossible(Long jobId);


    Response<Boolean> scheduleJob(Long jobId, List<String> servers);


    Response<Boolean> scheduleJobs(List<Long> jobIds, final List<String> servers);


    Response<Boolean> triggerJob(Long jobId);


    Response<Boolean> notifyJob(Long jobId);


    Response<Boolean> pauseJob(Long jobId);


    Response<Boolean> resumeJob(Long jobId);


    Response<Boolean> removeJob(Long jobId);


    Response<Boolean> reloadJob(Long jobId);
}
