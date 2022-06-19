package me.hao0.jbs.server.cluster.server;

import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.support.Lifecycle;
import me.hao0.jbs.common.support.Component;
import me.hao0.jbs.common.util.ZkPaths;
import me.hao0.jbs.store.support.JbsZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@org.springframework.stereotype.Component
@Lazy
public class ServerRegister extends Component implements Lifecycle{

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    private ServerHost serverHost;

    @Autowired
    private JbsZkClient zk;

    @Override
    public void doStart() {

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                String server = serverHost.get();


                zk.client().mkdirs(ZkPaths.SERVERS);


                String serverPath = ZkPaths.pathOfServer(server);

                if (!zk.client().checkExists(serverPath)){
                    String result = zk.client().createEphemeral(ZkPaths.pathOfServer(server));
                    Logs.info("server({}) registered: {}", server, result);
                }
            }
        }, 1, 5, TimeUnit.SECONDS);

    }

    @Override
    public void doShutdown() {
        scheduler.shutdown();
    }
}
