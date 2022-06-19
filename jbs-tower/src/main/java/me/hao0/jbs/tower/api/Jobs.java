package me.hao0.jbs.tower.api;

import me.hao0.jbs.common.dto.*;
import me.hao0.jbs.common.model.Job;
import me.hao0.jbs.common.model.JobConfig;
import me.hao0.jbs.common.model.JobDependence;
import me.hao0.jbs.common.model.JobInstanceShard;
import me.hao0.jbs.common.model.enums.JobInstanceShardStatus;
import me.hao0.jbs.common.model.enums.JobInstanceStatus;
import me.hao0.jbs.common.model.enums.JobTriggerType;
import me.hao0.jbs.common.util.CollectionUtil;
import me.hao0.jbs.common.util.Crons;
import me.hao0.jbs.store.service.JobService;
import me.hao0.jbs.store.service.ServerService;
import me.hao0.jbs.store.util.Page;
import me.hao0.jbs.common.util.Response;
import me.hao0.jbs.tower.support.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/api/jobs")
public class Jobs {

    @Autowired
    private ServerService serverService;

    @Autowired
    private JobService jobService;

    @Autowired
    private Messages messages;


    @RequestMapping(method = RequestMethod.GET)
    public JsonResponse pagingJobs(
            @RequestParam("appId") Long appId,
            @RequestParam(value = "jobClass", defaultValue = "") String jobClass,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){

        Response<Page<Job>> pagingResp = jobService.pagingJob(appId, jobClass, pageNo, pageSize);
        if(!pagingResp.isSuccess()){
            return JsonResponse.notOk(messages.get(pagingResp.getErr()));
        }

        return JsonResponse.ok(pagingResp.getData());
    }


    @RequestMapping(value = "/controls", method = RequestMethod.GET)
    public JsonResponse pagingJobControls(
            @RequestParam("appId") Long appId,
            @RequestParam(value = "jobClass", defaultValue = "") String jobClass,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){

        Response<Page<JobControl>> pagingResp = jobService.pagingJobControl(appId, jobClass, pageNo, pageSize);
        if(!pagingResp.isSuccess()){
            return JsonResponse.notOk(messages.get(pagingResp.getErr()));
        }

        formatJobStateDesc(pagingResp.getData().getData());

        return JsonResponse.ok(pagingResp.getData());
    }

    private void formatJobStateDesc(List<JobControl> controls) {
        if (!CollectionUtil.isNullOrEmpty(controls)){
            for (JobControl control : controls){
                control.setStateDesc(messages.get(control.getStateDesc()));
            }
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResponse findJobDetail(@PathVariable("id") Long id){

        Response<JobDetail> findResp = jobService.findJobDetailById(id);
        if(!findResp.isSuccess()){
            return JsonResponse.notOk(messages.get(findResp.getErr()));
        }

        return JsonResponse.ok(findResp.getData());
    }


    @RequestMapping(value = "/{jobId}/config", method = RequestMethod.GET)
    public JsonResponse findJobConfig(@PathVariable("jobId") Long jobId){

        Response<JobConfig> findResp = jobService.findJobConfigByJobId(jobId);
        if(!findResp.isSuccess()){
            return JsonResponse.notOk(messages.get(findResp.getErr()));
        }

        return JsonResponse.ok(findResp.getData());
    }


    @RequestMapping(method = RequestMethod.POST)
    public JsonResponse saveJob(@RequestBody JobEditDto jobEditDto){

        if (!Crons.isValidExpression(jobEditDto.getCron())){
            return JsonResponse.notOk(messages.get("job.cron.invalid"));
        }

        Response<Long> saveResp = jobService.saveJob(jobEditDto);
        if(!saveResp.isSuccess()){
            return JsonResponse.notOk(messages.get(saveResp.getErr()));
        }

        Response<Boolean> opResp;
        if (jobEditDto.getStatus()){

            opResp = serverService.scheduleJobIfPossible(saveResp.getData());
        } else {

            opResp = serverService.removeJob(saveResp.getData());
        }

        if (!opResp.isSuccess() || !opResp.getData()){
            return JsonResponse.notOk(messages.get(opResp.getErr()));
        }

        return JsonResponse.ok(opResp.getData());
    }


    @RequestMapping(value = "/instances", method = RequestMethod.GET)
    public JsonResponse pagingJobInstances(
            @RequestParam("appId") Long appId,
            @RequestParam("jobClass") String jobClass,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){

        Response<Page<JobInstanceDto>> pagingResp = jobService.pagingJobInstance(appId, jobClass, pageNo, pageSize);
        if (!pagingResp.isSuccess()){
            return JsonResponse.notOk(messages.get(pagingResp.getErr()));
        }

        formatJobInstances(pagingResp.getData().getData());

        return JsonResponse.ok(pagingResp.getData());
    }

    private void formatJobInstances(List<JobInstanceDto> instances) {

        if (!CollectionUtil.isNullOrEmpty(instances)){
            for (JobInstanceDto instance : instances){
                instance.setStatusDesc(messages.get(JobInstanceStatus.from(instance.getStatus()).code()));
                instance.setTriggerTypeDesc(messages.get(JobTriggerType.from(instance.getTriggerType()).code()));
            }
        }

    }


    @RequestMapping(value = "/instances/{jobInstanceId}/shards", method = RequestMethod.GET)
    public JsonResponse pagingJobInstanceShards(
            @PathVariable("jobInstanceId") Long jobInstanceId,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){

        Response<Page<JobInstanceShardDto>> pagingResp = jobService.pagingJobInstanceShards(jobInstanceId, pageNo, pageSize);
        if (!pagingResp.isSuccess()){
            return JsonResponse.notOk(messages.get(pagingResp.getErr()));
        }

        formatShardStatus(pagingResp.getData().getData());

        return JsonResponse.ok(pagingResp.getData());
    }

    private void formatShardStatus(List<JobInstanceShardDto> shards) {

        if (CollectionUtil.isNullOrEmpty(shards)){
            return;
        }

        JobInstanceShardStatus shardStatus;
        for (JobInstanceShardDto shard : shards){
            shardStatus = JobInstanceShardStatus.from(shard.getStatus());
            shard.setStatusDesc(messages.get(shardStatus.code()));
        }
    }



    @RequestMapping(value = "/instance_shards/{id}", method = RequestMethod.GET)
    public JsonResponse findJobInstanceShard(@PathVariable("id") Long id){

        Response<JobInstanceShard> findResp = jobService.findJobInstanceShardById(id);
        if (!findResp.isSuccess()){
            return JsonResponse.notOk(messages.get(findResp.getErr()));
        }

        return JsonResponse.ok(findResp.getData());
    }


    @RequestMapping(value = "/{jobId}/trigger", method = RequestMethod.POST)
    public JsonResponse triggerJob(@PathVariable("jobId") Long jobId){
        Response<Boolean> triggerResp = serverService.triggerJob(jobId);
        if (!triggerResp.isSuccess() || !triggerResp.getData()){
            return JsonResponse.notOk(messages.get(triggerResp.getErr()));
        }

        return JsonResponse.ok(triggerResp.getData());
    }


    @RequestMapping(value = "/{jobId}/pause", method = RequestMethod.POST)
    public JsonResponse pauseJob(@PathVariable("jobId") Long jobId){
        Response<Boolean> pauseResp = serverService.pauseJob(jobId);
        if (!pauseResp.isSuccess() || !pauseResp.getData()){
            return JsonResponse.notOk(messages.get(pauseResp.getErr()));
        }

        return JsonResponse.ok(pauseResp.getData());
    }


    @RequestMapping(value = "/{jobId}/resume", method = RequestMethod.POST)
    public JsonResponse resumeJob(@PathVariable("jobId") Long jobId){
        Response<Boolean> resumeResp = serverService.resumeJob(jobId);
        if (!resumeResp.isSuccess() || !resumeResp.getData()){
            return JsonResponse.notOk(messages.get(resumeResp.getErr()));
        }

        return JsonResponse.ok(resumeResp.getData());
    }


    @RequestMapping(value = "/{jobId}/disable", method = RequestMethod.POST)
    public JsonResponse disableJob(@PathVariable("jobId") Long jobId){


        Response<Boolean> disableResp = jobService.disableJob(jobId);
        if (!disableResp.isSuccess() || !disableResp.getData()){
            return JsonResponse.notOk(messages.get(disableResp.getErr()));
        }


        Response<Boolean> removeResp = serverService.removeJob(jobId);
        if (!removeResp.isSuccess() || !removeResp.isSuccess()){
            return JsonResponse.notOk(messages.get(removeResp.getErr()));
        }

        return JsonResponse.ok();
    }


    @RequestMapping(value = "/{jobId}/enable", method = RequestMethod.POST)
    public JsonResponse enableJob(@PathVariable("jobId") Long jobId){


        Response<Boolean> enableResp = jobService.enableJob(jobId);
        if (!enableResp.isSuccess()){
            return JsonResponse.notOk(messages.get(enableResp.getErr()));
        }


        Response<Boolean> schedulingResp = serverService.scheduleJobIfPossible(jobId);
        if (!schedulingResp.isSuccess() || !schedulingResp.getData()){
            return JsonResponse.notOk(messages.get(schedulingResp.getErr()));
        }

        return JsonResponse.ok();
    }


    @RequestMapping(value = "/{jobId}/schedule", method = RequestMethod.POST)
    public JsonResponse scheduleJob(@PathVariable("jobId") Long jobId){


        Response<Boolean> scheduleResp = serverService.scheduleJob(jobId);
        if (!scheduleResp.isSuccess() || !scheduleResp.isSuccess()){
            return JsonResponse.notOk(messages.get(scheduleResp.getErr()));
        }

        return JsonResponse.ok();
    }


    @RequestMapping(value = "/{jobId}/stop", method = RequestMethod.POST)
    public JsonResponse stopJob(@PathVariable("jobId") Long jobId){


        Response<Boolean> removeResp = serverService.removeJob(jobId);
        if (!removeResp.isSuccess() || !removeResp.isSuccess()){
            return JsonResponse.notOk(messages.get(removeResp.getErr()));
        }

        return JsonResponse.ok();
    }


    @RequestMapping(value = "/{jobId}/delete", method = RequestMethod.POST)
    public JsonResponse deleteJob(@PathVariable("jobId") Long jobId){


        Response<Boolean> removeResp = serverService.removeJob(jobId);
        if (!removeResp.isSuccess() || !removeResp.getData()){
            return JsonResponse.notOk(messages.get(removeResp.getErr()));
        }


        Response<Boolean> deleteResp = jobService.deleteJob(jobId);
        if (!deleteResp.isSuccess() || !deleteResp.getData()){
            return JsonResponse.notOk(messages.get(deleteResp.getErr()));
        }

        return JsonResponse.ok();
    }


    @RequestMapping(value = "/{jobId}/terminate")
    public JsonResponse terminateJob(@PathVariable("jobId") Long jobId){

        Response<Boolean> terminateResp = jobService.terminateJob(jobId);
        if (!terminateResp.isSuccess()){
            return JsonResponse.notOk(messages.get(terminateResp.getErr()));
        }

        return JsonResponse.ok(terminateResp.getData());
    }


    @RequestMapping(value = "/{jobId}/monitor")
    public JsonResponse monitorJob(@PathVariable("jobId") Long jobId){

        Response<JobInstanceDetail> findResp = jobService.monitorJobInstanceDetail(jobId);
        if (!findResp.isSuccess()){
            return JsonResponse.notOk(messages.get(findResp.getErr()));
        }

        formatJobInstanceStatus(findResp.getData());

        return JsonResponse.ok(findResp.getData());
    }


    @RequestMapping(value = "/instances/{id}", method = RequestMethod.GET)
    public JsonResponse findJobInstanceDetail(@PathVariable("id") Long id){

        Response<JobInstanceDetail> findResp = jobService.findJobInstanceDetail(id);
        if(!findResp.isSuccess()){
            return JsonResponse.notOk(messages.get(findResp.getErr()));
        }

        formatJobInstanceStatus(findResp.getData());

        return JsonResponse.ok(findResp.getData());
    }

    private void formatJobInstanceStatus(JobInstanceDetail detail){
        if (detail != null){
            JobInstanceStatus status = JobInstanceStatus.from(detail.getStatus());
            detail.setStatusDesc(messages.get(status.code()));
        }
    }


    @RequestMapping(value = "/{jobId}/next", method = RequestMethod.POST)
    public JsonResponse addDependenceJob(
                @PathVariable("jobId") Long jobId,
                @RequestBody SaveNextJob nextJob){

        JobDependence dependence = new JobDependence();
        dependence.setJobId(jobId);
        dependence.setNextJobId(nextJob.getNextJobId());

        Response<Boolean> addResp = jobService.addJobDependence(dependence);
        if (!addResp.isSuccess()){
            return JsonResponse.notOk(messages.get(addResp.getErr()));
        }

        return JsonResponse.ok(addResp.getData());
    }


    @RequestMapping(value = "/{jobId}/next", method = RequestMethod.GET)
    public JsonResponse pagingDependenceJobs(
        @PathVariable("jobId") Long jobId,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){

        Response<Page<DependenceJob>> pagingResp = jobService.pagingNextJobs(jobId, pageNo, pageSize);
        if (!pagingResp.isSuccess()){
            return JsonResponse.notOk(messages.get(pagingResp.getErr()));
        }

        return JsonResponse.ok(pagingResp.getData());
    }


    @RequestMapping(value = "/{jobId}/del_next/{nextJobId}", method = RequestMethod.POST)
    public JsonResponse deleteDependenceJob(
            @PathVariable("jobId") Long jobId,
            @PathVariable("nextJobId") Long nextJobId){

        Response<Boolean> deleteResp = jobService.deleteNextJob(jobId, nextJobId);
        if (!deleteResp.isSuccess()){
            return JsonResponse.notOk(messages.get(deleteResp.getErr()));
        }

        return JsonResponse.ok(deleteResp.getData());
    }


    @RequestMapping(value = "/{jobId}/assigns", method = RequestMethod.GET)
    public JsonResponse listJobAssigns(@PathVariable("jobId") Long jobId){

        Response<List<JobAssignDto>> listResp = jobService.listJobAssigns(jobId);
        if (!listResp.isSuccess()){
            return JsonResponse.notOk(messages.get(listResp.getErr()));
        }

        return JsonResponse.ok(listResp.getData());
    }


    @RequestMapping(value = "/{jobId}/assigns", method = RequestMethod.POST)
    public JsonResponse saveJobAssign(
            @PathVariable("jobId") Long jobId,
            @RequestBody JobAssignSaveDto saveDto){

        Response<Boolean> saveResp = jobService.saveJobAssign(jobId, saveDto.getAssignIps());
        if (!saveResp.isSuccess()){
            return JsonResponse.notOk(messages.get(saveResp.getErr()));
        }

        return JsonResponse.ok();
    }

}
