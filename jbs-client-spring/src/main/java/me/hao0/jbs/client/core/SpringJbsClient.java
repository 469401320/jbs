package me.hao0.jbs.client.core;

import me.hao0.jbs.client.job.DefaultJob;
import me.hao0.jbs.client.job.script.ScriptJob;
import me.hao0.jbs.common.util.CollectionUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;


public class SpringJbsClient implements InitializingBean, DisposableBean {

    @Autowired
    private ApplicationContext springContext;

    private final SimpleJbsClient client;

    public SpringJbsClient(String appName, String appSecret, String zkServers) {
        this(appName, appSecret, zkServers, null);
    }

    public SpringJbsClient(String appName, String appSecret, String zkServers, String zkNamespace){
        client = new SimpleJbsClient(appName, appSecret, zkServers, zkNamespace);
    }

    public void setExecutorThreadCount(Integer executorThreadCount) {
        client.setExecutorThreadCount(executorThreadCount);
    }

    @Override
    public void afterPropertiesSet() throws Exception {


        client.start();


        registerJobs();
    }

    private void registerJobs() {


        Map<String, DefaultJob> defaultJobs = springContext.getBeansOfType(DefaultJob.class);
        if (!CollectionUtil.isNullOrEmpty(defaultJobs)){
            for (DefaultJob defaultJob : defaultJobs.values()){
                client.registerJob(defaultJob);
            }
        }


        Map<String, ScriptJob> scriptJobs = springContext.getBeansOfType(ScriptJob.class);
        if (!CollectionUtil.isNullOrEmpty(scriptJobs)){
            for (ScriptJob scriptJob : scriptJobs.values()){
                client.registerJob(scriptJob);
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        client.shutdown();
    }
}
