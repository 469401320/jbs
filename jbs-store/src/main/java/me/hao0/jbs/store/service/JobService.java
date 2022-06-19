package me.hao0.jbs.store.service;

import me.hao0.jbs.common.dto.*;
import me.hao0.jbs.common.model.Job;
import me.hao0.jbs.common.model.JobConfig;
import me.hao0.jbs.common.model.JobDependence;
import me.hao0.jbs.common.model.JobInstance;
import me.hao0.jbs.common.model.JobInstanceShard;
import me.hao0.jbs.store.util.Page;
import me.hao0.jbs.common.util.Response;
import java.util.List;
import java.util.Set;


public interface JobService {


    Response<Long> saveJob(JobEditDto editing);


    Response<Long> saveJobDetail(JobDetail jobDetail);


    Response<Boolean> deleteJob(Long jobId);


    Response<Job> findJobById(Long jobId);


    Response<JobDetail> findJobDetailById(Long jobId);


    Response<Page<Job>> pagingJob(Long appId, String jobClass, Integer pageNo, Integer pageSize);


    Response<Page<JobControl>> pagingJobControl(Long appId, String jobClass, Integer pageNo, Integer pageSize);


    Response<Boolean> createJobInstance(JobInstance instance);


    Response<Boolean> failedJobInstance(Long jobInstanceId, String cause);


    Response<JobInstance> findJobInstanceById(Long instanceId);


    Response<Page<JobInstanceDto>> pagingJobInstance(Long appId, String jobClass, Integer pageNo, Integer pageSize);


    Response<Page<JobInstanceShardDto>> pagingJobInstanceShards(Long jobinstanceId, Integer pageNo, Integer pageSize);


    Response<List<JobDetail>> findValidJobsByServer(String server);


    Response<Boolean> createJobInstanceAndShards(JobInstance instance, JobConfig config);


    Response<PullShard> pullJobInstanceShard(Long jobInstanceId, String client);


    Response<Boolean> returnJobInstanceShard(Long jobInstanceId, Long shardId, String client);


    Response<Boolean> finishJobInstanceShard(ShardFinishDto shardFinishDto);


    Response<Boolean> returnJobInstanceShardsOfClient(String client);


    Response<JobInstanceShard> findJobInstanceShardById(Long shardId);


    Response<List<Long>> findJobIdsByServer(String server);


    Response<List<Job>> findJobsByServer(String server);


    Response<Boolean> removeAllJobsByServer(String server);


    Response<Boolean> bindJob2Server(Long jobId, String server);


    Response<String> findServerOfJob(Long jobId);


    Response<JobConfig> findJobConfigByJobId(Long jobId);


    Response<Boolean> disableJob(Long jobId);


    Response<Boolean> enableJob(Long jobId);


    Response<JobInstanceDetail> monitorJobInstanceDetail(Long jobId);


    Response<JobInstanceDetail> findJobInstanceDetail(Long jobInstanceId);


    Response<Boolean> terminateJob(Long jobId);


    Response<Boolean> unbindJobServer(String server, Long jobId);


    Response<Boolean> addJobDependence(JobDependence dependence);


    Response<Boolean> deleteNextJob(Long jobId, Long nextJobId);


    Response<Boolean> deleteNextJobs(Long jobId);


    Response<Page<DependenceJob>> pagingNextJobs(Long jobId, Integer pageNo, Integer pageSize);


    Response<Page<Long>> pagingNextJobIds(Long jobId, Integer pageNo, Integer pageSize);


    Response<List<JobAssignDto>> listJobAssigns(Long jobId);


    Response<Boolean> saveJobAssign(Long jobId, String clientIps);


    Response<Set<String>> listSimpleJobAssigns(Long jobId);

}
