package me.hao0.jbs.client.core;

import me.hao0.jbs.common.support.Lifecycle;
import me.hao0.jbs.common.support.Component;
import me.hao0.jbs.common.util.Systems;
import me.hao0.jbs.common.util.ZkPaths;
import me.hao0.jbs.common.zk.ZkClient;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ClientRegister extends Component implements Lifecycle {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final AbstractJbsClient client;

    public ClientRegister(AbstractJbsClient client) {
        this.client = client;
    }

    @Override
    public void doStart() {

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                ZkClient zk = client.getZk();


                String appClientPath = ZkPaths.pathOfAppClient(client.getAppName(), Systems.hostPid());
                if (!zk.checkExists(appClientPath)){
                    zk.createEphemeral(appClientPath);
                }

            }
        }, 1, 10, TimeUnit.SECONDS);
    }

    @Override
    public void doShutdown() {
        scheduler.shutdown();
    }
}
