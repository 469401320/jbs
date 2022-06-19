package me.hao0.jbs.client.core;

import com.google.common.base.Strings;
import me.hao0.jbs.client.job.*;
import me.hao0.jbs.client.job.execute.JobExecutor;
import me.hao0.jbs.client.job.execute.SimpleJobExecutor;
import me.hao0.jbs.common.support.Component;
import me.hao0.jbs.common.util.ZkPaths;
import me.hao0.jbs.common.zk.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


abstract class AbstractJbsClient extends Component implements JbsClient {

    private static final Logger log = LoggerFactory.getLogger(AbstractJbsClient.class);


    private final String CLIENT_VERSION = "1.0.0";


    private final String appName;


    private final String appSecret;


    private final String zkServers;


    private Integer executorThreadCount = 32;


    private final List<String> httpServers = new CopyOnWriteArrayList<>();


    private final String zkNamespace;


    private final JbsHttpAgent http = new JbsHttpAgent(this);


    private final JbsZkAgent zk;


    private final JobManager jobManager = new JobManager(this);


    private JobExecutor jobExecutor = new SimpleJobExecutor(this);

    public AbstractJbsClient(String appName, String zkServers) {
        this(appName, null, zkServers);
    }

    public AbstractJbsClient(String appName, String appSecret, String zkServers) {
        this(appName, appSecret, zkServers, null);
    }

    public AbstractJbsClient(String appName, String appSecret, String zkServers, String zkNamespace) {
        this.appName = appName;
        this.appSecret = appSecret;
        this.zkServers = zkServers;
        this.zkNamespace = Strings.isNullOrEmpty(zkNamespace) ? ZkPaths.DEFAULT_NS : zkNamespace;
        zk = new JbsZkAgent(this, zkServers, this.zkNamespace);
    }

    @Override
    public String getClientVersion() {
        return CLIENT_VERSION;
    }

    @Override
    public void setJobExecutor(JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getZkNamespace() {
        return zkNamespace;
    }

    public String getZkServers() {
        return zkServers;
    }

    public Integer getExecutorThreadCount() {
        return executorThreadCount;
    }

    public void setExecutorThreadCount(Integer executorThreadCount) {
        this.executorThreadCount = executorThreadCount;
    }

    public ZkClient getZk() {
        return zk.client();
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public JobExecutor getJobExecutor() {
        return jobExecutor;
    }

    public JbsHttpAgent getHttp() {
        return http;
    }

    public List<String> getHttpServers() {
        return httpServers;
    }

    public void addHttpServer(String httpServer) {
        if (!this.httpServers.contains(httpServer)){
            this.httpServers.add(httpServer);
        }
    }

    public void removeHttpServer(String httpServer){
        this.httpServers.remove(httpServer);
    }

    @Override
    public void doStart(){

        zk.start();

        http.start();

        jobExecutor.start();

        jobManager.start();

        afterStart();

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                shutdown();
            }
        });

        log.info("Jbs client started successfully.");
    }


    @Override
    public void doShutdown(){

        zk.shutdown();

        http.shutdown();

        jobManager.shutdown();

        jobExecutor.shutdown();

        afterShutdown();

        log.info("Jbs client shutdown finished.");
    }

    @Override
    public void registerJob(Job job){
        jobManager.registerJob(job);
    }


    protected abstract void afterStart();


    protected abstract void afterShutdown();
}
