package me.hao0.jbs.server.schedule;

import me.hao0.jbs.common.dto.JobDetail;
import me.hao0.jbs.common.dto.JobFireTime;
import me.hao0.jbs.common.model.App;
import me.hao0.jbs.common.model.Job;
import me.hao0.jbs.common.model.enums.JobState;
import me.hao0.jbs.common.support.Lifecycle;
import me.hao0.jbs.common.support.Component;
import me.hao0.jbs.store.support.JobSupport;


public class ZkJob extends Component implements Lifecycle, ScheduleJob {

    private final JobSupport jobSupport;


    private final JobDetail jobDetail;


    private final String scheduler;


    private final JobFireTime jobFireTime;

    public ZkJob(JobSupport jobSupport, JobDetail jobDetail, String scheduler, JobFireTime jobFireTime) {
        this.jobSupport = jobSupport;
        this.jobDetail = jobDetail;
        this.scheduler = scheduler;
        this.jobFireTime = jobFireTime;
    }

    @Override
    public JobDetail getJobDetail() {
        return jobDetail;
    }

    @Override
    public void doStart() {


        initJobNodes();
    }

    @Override
    public void doShutdown() {
    }


    private void initJobNodes() {

        App app = jobDetail.getApp();

        Job job = jobDetail.getJob();

        String appName = app.getAppName();
        String jobClass = job.getClazz();


        jobSupport.deleteJobInstances(appName, jobClass);


        jobSupport.mkJobInstances(appName, jobClass);


        jobSupport.updateJobStateDirectly(appName, jobClass, JobState.WAITING);


        jobSupport.updateJobScheduler(appName, jobClass, scheduler);


        jobSupport.updateJobFireTime(appName, jobClass, jobFireTime);
    }
}
