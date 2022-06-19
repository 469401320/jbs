package me.hao0.jbs.server.cluster.client;

import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.support.Lifecycle;
import me.hao0.jbs.common.support.Component;
import me.hao0.jbs.store.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;


@org.springframework.stereotype.Component
public class ClientFailover extends Component implements Lifecycle {

    @Autowired
    private ClientCluster clientCluster;

    @Autowired
    private JobService jobService;

    private static final String CLIENT_LISTENER_ID = "ClientFailover";

    @Override
    public void doStart() {
        clientCluster.addListener(CLIENT_LISTENER_ID, new AppClientChangedListener() {
            @Override
            public void onChanged(String appName, String client, Boolean join) {
                if(!join){
                    Logs.warn("The app({})'s client({}) left, will do failover.", appName, client);

                    doFailover(client);
                }
            }
        });
    }

    private void doFailover(String client) {

        jobService.returnJobInstanceShardsOfClient(client);
    }

    @Override
    public void doShutdown() {
        clientCluster.removeListener(CLIENT_LISTENER_ID);
    }
}
