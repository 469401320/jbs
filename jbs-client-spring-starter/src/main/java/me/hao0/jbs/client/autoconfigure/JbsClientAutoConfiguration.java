package me.hao0.jbs.client.autoconfigure;

import me.hao0.jbs.client.core.SimpleJbsClient;
import me.hao0.jbs.client.job.DefaultJob;
import me.hao0.jbs.client.job.script.ScriptJob;
import me.hao0.jbs.common.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;


@Configuration
@EnableConfigurationProperties(JbsClientProperties.class)
public class JbsClientAutoConfiguration {

    @Autowired
    private JbsClientProperties properties;

    @Autowired
    private ApplicationContext springContext;

    @Bean(destroyMethod = "shutdown")
    public SimpleJbsClient buildSpringClient(){

        SimpleJbsClient client = new SimpleJbsClient(
                                            properties.getAppName(),
                                            properties.getAppSecret(),
                                            properties.getZkServers(),
                                            properties.getZkNamespace());

        client.setExecutorThreadCount(properties.getExecutorThreadCount());

        client.start();

        registerJobs(client);

        return client;
    }

    private void registerJobs(final SimpleJbsClient client) {


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
}
