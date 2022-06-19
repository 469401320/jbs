package me.hao0.jbs.server.api;

import me.hao0.jbs.common.model.enums.JobTriggerType;
import me.hao0.jbs.server.cluster.server.ServerHost;
import me.hao0.jbs.server.schedule.JobPool;
import me.hao0.jbs.store.service.JobService;
import me.hao0.jbs.common.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import static me.hao0.jbs.store.util.ServerUris.*;


@RestController
@RequestMapping(value = SERVERS)
public class Servers {

    @Autowired
    private JobPool jobPool;

    @Autowired
    private JobService jobService;

    @Autowired
    private ServerHost host;

    @RequestMapping(value = JOB_SCHEDULE + "/{jobId}", method = RequestMethod.POST)
    public Boolean scheduleJob(@PathVariable(value = "jobId") Long jobId){

        Response<Boolean> bindResp = jobService.bindJob2Server(jobId, host.get());
        if (!bindResp.isSuccess()){
            return Boolean.FALSE;
        }


        return jobPool.scheduleJob(jobId);
    }


    @RequestMapping(value = JOB_TRIGGER + "/{jobId}", method = RequestMethod.POST)
    public Boolean triggerJob(@PathVariable(value = "jobId") Long jobId){
        return jobPool.triggerJob(jobId, JobTriggerType.API);
    }


    @RequestMapping(value = JOB_NOTIFY + "/{jobId}", method = RequestMethod.POST)
    public Boolean notifyJob(@PathVariable(value = "jobId") Long jobId){
        return jobPool.triggerJob(jobId, JobTriggerType.NOTIFY);
    }


    @RequestMapping(value = JOB_PAUSE + "/{jobId}", method = RequestMethod.POST)
    public Boolean pauseJob(@PathVariable(value = "jobId") Long jobId){
        return jobPool.pauseJob(jobId);
    }


    @RequestMapping(value = JOB_RESUME + "/{jobId}", method = RequestMethod.POST)
    public Boolean resumeJob(@PathVariable(value = "jobId") Long jobId){
        return jobPool.resumeJob(jobId);
    }


    @RequestMapping(value = JOB_REMOVE + "/{jobId}", method = RequestMethod.POST)
    public Boolean removeJob(@PathVariable(value = "jobId") Long jobId){
        return jobPool.removeJob(jobId);
    }


    @RequestMapping(value = JOB_RELOAD + "/{jobId}", method = RequestMethod.POST)
    public Boolean reloadJob(@PathVariable(value = "jobId") Long jobId){
        return jobPool.reloadJob(jobId);
    }
}
