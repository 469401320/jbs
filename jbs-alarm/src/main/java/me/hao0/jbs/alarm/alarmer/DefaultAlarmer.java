package me.hao0.jbs.alarm.alarmer;

import me.hao0.jbs.common.model.AlarmEvent;
import me.hao0.jbs.common.util.Response;
import me.hao0.jbs.store.service.AlarmService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;


@Component
public class DefaultAlarmer extends AbstractAlarmer implements Alarmer, InitializingBean, DisposableBean {

    private ScheduledExecutorService scheduler;

    private final Integer PULL_BATCH_SIZE = 100;

    @Autowired
    private AlarmService alarmService;

    @Override
    public void afterPropertiesSet() throws Exception {

        if (!alarmConfig.getEnable()){
            return;
        }


        scheduler = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("ALARM-WORKER");
                t.setDaemon(true);
                return t;
            }
        });

        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Response<List<AlarmEvent>> pullResp = alarmService.pull(PULL_BATCH_SIZE);
                if (pullResp.isSuccess()){
                    if (!pullResp.getData().isEmpty()){
                        for (AlarmEvent event : pullResp.getData()){
                            alarm(event);
                        }
                    }
                }
            }
        }, 1L, 5, TimeUnit.SECONDS);
    }

    @Override
    public void destroy() throws Exception {
        scheduler.shutdown();
    }
}
