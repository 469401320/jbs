package me.hao0.jbs.server.schedule.executor;

import me.hao0.jbs.common.dto.JobDetail;
import me.hao0.jbs.common.model.enums.JobTriggerType;
import org.quartz.JobExecutionContext;


public interface JobExecutor {


    void execute(JobDetail jobDetail, JobTriggerType triggerType, JobExecutionContext context);
}
