package me.hao0.jbs.client.job;

import com.google.common.collect.Maps;
import me.hao0.jbs.client.core.JbsClient;
import me.hao0.jbs.client.job.execute.ZkJob;
import me.hao0.jbs.common.support.Lifecycle;
import me.hao0.jbs.common.support.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class JobManager extends Component implements Lifecycle {

    private static final Logger log = LoggerFactory.getLogger(JobManager.class);

    private final JbsClient client;

    private Map<String, ZkJob> jobs = Maps.newConcurrentMap();

    public JobManager(JbsClient client) {
        this.client = client;
    }

    @Override
    public void doStart() {
        if (!jobs.isEmpty()){
            for (ZkJob job : jobs.values()){
                job.start();
            }
        }
    }

    @Override
    public void doShutdown() {
        if (!jobs.isEmpty()){
            for (ZkJob job : jobs.values()){
                job.shutdown();
            }
        }
    }


    public void registerJob(Job job) {

        final String jobClass = job.getClass().getName();

        if (jobs.containsKey(jobClass)) return;

        ZkJob zkJob = new ZkJob(client, job);
        zkJob.start();

        log.info("registered the job: {}", job);

        jobs.put(jobClass, zkJob);
    }
}
