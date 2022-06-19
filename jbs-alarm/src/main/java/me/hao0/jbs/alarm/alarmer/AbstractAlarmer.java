package me.hao0.jbs.alarm.alarmer;

import com.google.common.base.Objects;
import me.hao0.jbs.alarm.config.AlarmConfig;
import me.hao0.jbs.alarm.notify.NotifierManager;
import me.hao0.jbs.common.dto.JobDetail;
import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.model.AlarmEvent;
import me.hao0.jbs.common.model.enums.AlarmEventType;
import me.hao0.jbs.common.model.enums.AlarmNotifyType;
import me.hao0.jbs.common.util.Response;
import me.hao0.jbs.store.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractAlarmer implements Alarmer {

    @Autowired
    protected AlarmConfig alarmConfig;

    @Autowired
    protected JobService jobService;

    @Autowired
    protected NotifierManager notifierManager;

    @Override
    public Boolean filter(AlarmEvent e) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean alarm(AlarmEvent e) {


        if (!filter(e)){
            return Boolean.TRUE;
        }


        AlarmContext context = buildAlarmContext(e);
        if (context == null){
            return Boolean.FALSE;
        }


        AlarmNotifyType notifyType = AlarmNotifyType.from(alarmConfig.getNotifyType());

        return notifierManager.notify(notifyType, context);
    }

    private AlarmContext buildAlarmContext(AlarmEvent e) {

        AlarmContext context = new AlarmContext();

        Response<JobDetail> findResp = jobService.findJobDetailById(e.getJobId());
        if (!findResp.isSuccess()){
            Logs.error("failed to find job detail(event={}), cause: {}", e, findResp.getErr());
            return null;
        }
        if (findResp.getData() == null){
            Logs.warn("can't find the job detail(event={})", e);
            return null;
        }

        JobDetail jobDetail = findResp.getData();
        context.setAppName(jobDetail.getApp().getAppName());
        context.setJobName(jobDetail.getJob().getClazz());

        Response<String> schedulerResp = jobService.findServerOfJob(e.getJobId());
        if (!schedulerResp.isSuccess()){
            Logs.error("failed to find the job(event={})'s scheduler, cause: {}", e, schedulerResp.getErr());
        }
        context.setScheduler(schedulerResp.getData());

        context.setDetail(e.getDetail());

        context.setSubject(alarmConfig.getSubject());


        String template;
        if (Objects.equal(AlarmEventType.JOB_TIMEOUT.value(), e.getType())){
            template = alarmConfig.getJobTimeoutTemplate();
        } else if (Objects.equal(AlarmEventType.JOB_FAILED.value(), e.getType())){
            template = alarmConfig.getJobFailedTemplate();
        } else {
            throw new IllegalStateException("Not support alarm event type: " + e.getType());
        }

        context.setBody(
            template.replace("{appName}", context.getAppName())
                    .replace("{jobClass}", context.getJobName())
                    .replace("{scheduler}", context.getScheduler())
                    .replace("{detail}", context.getDetail())
        );

        return context;
    }
}
