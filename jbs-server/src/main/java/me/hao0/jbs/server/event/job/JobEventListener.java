package me.hao0.jbs.server.event.job;

import com.google.common.eventbus.Subscribe;
import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.model.AlarmEvent;
import me.hao0.jbs.common.model.enums.AlarmEventType;
import me.hao0.jbs.common.util.CollectionUtil;
import me.hao0.jbs.server.event.core.EventListener;
import me.hao0.jbs.store.service.AlarmService;
import me.hao0.jbs.store.service.JobService;
import me.hao0.jbs.store.service.ServerService;
import me.hao0.jbs.store.util.Page;
import me.hao0.jbs.common.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class JobEventListener implements EventListener {

    @Autowired
    private JobService jobService;

    @Autowired
    private ServerService serverService;

    @Autowired
    private AlarmService alarmService;

    @Subscribe
    public void onJobFinished(JobFinishedEvent e){

        notifyAllNextJobs(e);
    }

    private void notifyAllNextJobs(JobFinishedEvent e) {

        int pageNo = 1;
        int pageSize = 100;
        Long jobId = e.getJobId();

        Response<Page<Long>> nextJobIdsResp;
        for (;;){

            nextJobIdsResp = jobService.pagingNextJobIds(jobId, pageNo, pageSize);
            if (!nextJobIdsResp.isSuccess()){
                Logs.error("failed to paging next job ids(jobId={}, pageNo={}, pageSize={}) when notify all next jobs, cause: {}",
                        jobId, pageNo, pageSize, nextJobIdsResp.getErr());
                break;
            }

            List<Long> nextJobIds = nextJobIdsResp.getData().getData();
            if (CollectionUtil.isNullOrEmpty(nextJobIds)){

                break;
            }

            for (Long nextJobId : nextJobIds){
                serverService.notifyJob(nextJobId);
            }

            pageNo ++;
        }
    }

    @Subscribe
    public void onJobTimeout(JobTimeoutEvent e){

        Logs.warn("There is an job timeout event({}).", e);

        AlarmEvent timeoutEvent = buildAlarmEvent(e, AlarmEventType.JOB_TIMEOUT);
        timeoutEvent.setDetail(e.getDetail());

        Response<Boolean> pushResp = alarmService.push(timeoutEvent);
        if (!pushResp.isSuccess()){
            Logs.warn("failed to push job timeout event({}), cause: {}", timeoutEvent, pushResp.getErr());
        }
    }

    @Subscribe
    public void onJobFailed(JobFailedEvent e){

        Logs.warn("There is an job failed event({}).", e);

        AlarmEvent failedEvent = buildAlarmEvent(e, AlarmEventType.JOB_FAILED);
        failedEvent.setDetail(e.getCause());

        Response<Boolean> pushResp = alarmService.push(failedEvent);
        if (!pushResp.isSuccess()){
            Logs.warn("failed to push job failed event({}), cause: {}", failedEvent, pushResp.getErr());
        }
    }

    private AlarmEvent buildAlarmEvent(JobEvent e, AlarmEventType type) {

        AlarmEvent alarmEvent = new AlarmEvent();

        alarmEvent.setJobId(e.getJobId());
        alarmEvent.setType(type.value());

        return alarmEvent;
    }
}
