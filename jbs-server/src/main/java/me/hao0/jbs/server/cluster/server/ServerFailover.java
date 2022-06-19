package me.hao0.jbs.server.cluster.server;

import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.support.Lifecycle;
import me.hao0.jbs.common.support.Component;
import me.hao0.jbs.common.util.CollectionUtil;
import me.hao0.jbs.common.util.Sleeps;
import me.hao0.jbs.common.util.ZkPaths;
import me.hao0.jbs.common.zk.Lock;
import me.hao0.jbs.store.service.JobService;
import me.hao0.jbs.store.service.ServerService;
import me.hao0.jbs.store.support.JbsZkClient;
import me.hao0.jbs.common.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;


@org.springframework.stereotype.Component
public class ServerFailover extends Component implements Lifecycle{

    @Autowired
    private JobService jobService;

    @Autowired
    private ServerService serverService;

    @Autowired
    private ServerCluster serverCluster;

    @Autowired
    private JbsZkClient zk;

    @Value("${jbs.serverFailoverWaitTime:30}")
    private Integer serverFailoverWaitTime;

    @Override
    public void doStart() {
        serverCluster.addListener(new ServerChangedListener() {
            @Override
            public void onServerChanged(String server, Boolean join) {
                if (!join){

                    doFailOver(server);
                }
            }
        });
    }

    private void doFailOver(String server) {




        Lock lock = createServerFailoverLock(server);
        if(!lock.lock(1000)){

            return;
        }

        try {

            if (tryWaitServerStart(server)){
                return;
            }

            Logs.warn("The server({}) left, will do failover.", server);


            Response<List<Long>> jobIdsResp = jobService.findJobIdsByServer(server);
            if (!jobIdsResp.isSuccess()){
                Logs.warn("failed to find the server({})'s job ids, cause: {}", server, jobIdsResp.getErr());
                return;
            }

            List<Long> jobIds = jobIdsResp.getData();
            if (CollectionUtil.isNullOrEmpty(jobIds)){

                return;
            }


            serverService.scheduleJobs(jobIds, serverCluster.alives());

        } finally {
            lock.unlock();
        }
    }


    private Boolean tryWaitServerStart(String server) {

        Sleeps.sleep(serverFailoverWaitTime);


        String serverPath = ZkPaths.pathOfServer(server);
        return zk.client().checkExists(serverPath);
    }

    private Lock createServerFailoverLock(String server) {
        String serverFailoverLock = ZkPaths.pathOfServerFailoverLock(server);
        return zk.client().newLock(serverFailoverLock);
    }

    @Override
    public void doShutdown() {

    }
}
