package me.hao0.jbs.server;

import me.hao0.jbs.server.job.ContextJob;
import me.hao0.jbs.server.job.HelloJob;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;


public class QuartzTest {

    private Scheduler scheduler;

    @Before
    public void init() throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
    }

    @After
    public void destroy() throws SchedulerException {
        scheduler.shutdown();
    }

    @Test
    public void testCreateScheduler() throws SchedulerException {


        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();


        scheduler.start();


        scheduler.shutdown();
    }

    @Test
    public void testSimpleJob() throws SchedulerException, InterruptedException {
        JobDetail job = newJob(HelloJob.class)
                .withIdentity("helloJob", "test_app")
                .build();


        Trigger trigger = newTrigger()
                .withIdentity("helloJobTrigger", "test_app_triggers")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();

        scheduler.scheduleJob(job, trigger);

        Thread.sleep(10000000000000L);
    }

    @Test
    public void testSimpleJobJobWithContextParams() throws SchedulerException, InterruptedException {

        JobDetail job = newJob(ContextJob.class)
                .withIdentity("contextJob", "test_app")
                .usingJobData("jobSays", "Hello World!")
                .usingJobData("myFloatValue", 3.141f)
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("dumbJobTrigger", "test_app_triggers")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();

        scheduler.scheduleJob(job, trigger);

        Thread.sleep(10000000000000L);
    }

    @Test
    public void testCronJob() throws SchedulerException, InterruptedException {

        JobDetail job = newJob(HelloJob.class)
                .withIdentity("helloJob", "test_app").build();

        Trigger trigger = newTrigger()
                .withIdentity("trigger3", "group1")
                .withSchedule(cronSchedule("0/10 * * * * ?"))

                .build();

        scheduler.scheduleJob(job, trigger);

        Thread.sleep(10000000000000L);
    }

    @Test
    public void testMisfireJob() throws Exception {
        JobDetail job = newJob(HelloJob.class)
                .withIdentity("helloJob", "test_app").build();

        Trigger trigger = newTrigger()
                .withIdentity("trigger3", "group1")
                .withSchedule(cronSchedule("0/5 * * * * ?").withMisfireHandlingInstructionDoNothing())

                .build();

        scheduler.scheduleJob(job, trigger);

        Thread.sleep(10000000000000L);
    }
}
