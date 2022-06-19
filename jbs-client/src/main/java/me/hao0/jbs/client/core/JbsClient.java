package me.hao0.jbs.client.core;

import me.hao0.jbs.client.job.execute.JobExecutor;
import me.hao0.jbs.client.job.Job;
import me.hao0.jbs.client.job.JobManager;
import me.hao0.jbs.common.support.Lifecycle;
import me.hao0.jbs.common.zk.ZkClient;

import java.util.List;


public interface JbsClient extends Lifecycle {


    String getClientVersion();


    String getAppName();


    String getAppSecret();


    String getZkNamespace();


    String getZkServers();


    Integer getExecutorThreadCount();


    ZkClient getZk();


    JobManager getJobManager();


    JobExecutor getJobExecutor();

    void setJobExecutor(JobExecutor jobExecutor);


    JbsHttpAgent getHttp();


    List<String> getHttpServers();


    void addHttpServer(String httpServer);


    void removeHttpServer(String httpServer);


    void registerJob(Job job);
}
