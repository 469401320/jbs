package me.hao0.jbs.client.job.execute;

import com.google.common.base.Strings;
import me.hao0.jbs.client.core.JbsClient;
import me.hao0.jbs.client.job.Job;
import me.hao0.jbs.common.support.Lifecycle;
import me.hao0.jbs.common.support.Component;
import me.hao0.jbs.common.util.ZkPaths;
import me.hao0.jbs.common.zk.ChildListener;
import me.hao0.jbs.common.zk.ChildWatcher;

public class ZkJob extends Component implements Lifecycle {


    private Job job;


    private ChildWatcher watcher;

    private final JbsClient client;

    public ZkJob(JbsClient client, Job job) {
        this.client = client;
        this.job = job;
    }

    @Override
    public void doStart() {

        String appName = client.getAppName();

        String jobClass = getJobClass();

        String jobInstancesNodePath = ZkPaths.pathOfJobInstances(appName, jobClass);
        this.watcher = client.getZk().newChildWatcher(jobInstancesNodePath, new ChildListener() {
            @Override
            protected void onAdd(String path, byte[] data) {


                String instanceId = ZkPaths.lastNode(path);

                if (Strings.isNullOrEmpty(instanceId)) return;


                client.getJobExecutor().execute(Long.valueOf(instanceId), ZkJob.this);
            }
        });
    }

    public Job getJob() {
        return job;
    }

    public String getJobClass(){
        return job.getClass().getName();
    }

    @Override
    public void doShutdown() {
        watcher.stop();
    }
}