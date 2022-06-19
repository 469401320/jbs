package me.hao0.jbs.server.cluster.server;

import me.hao0.jbs.common.exception.ZkException;
import me.hao0.jbs.common.support.Lifecycle;
import me.hao0.jbs.common.support.Component;
import me.hao0.jbs.common.util.ZkPaths;
import me.hao0.jbs.common.zk.Lock;
import me.hao0.jbs.store.support.JbsZkClient;
import org.apache.curator.framework.recipes.locks.ChildReaper;
import org.apache.curator.framework.recipes.locks.Reaper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;


@org.springframework.stereotype.Component
public class ZkCleaner extends Component implements Lifecycle, DisposableBean {

    @Autowired
    private JbsZkClient zk;

    private ChildReaper emptyChildCleaner;

    @Override
    public void doStart() {
        String jobInstancesLockPath = Lock.PREFIX + ZkPaths.JOB_INSTANCES;
        zk.client().mkdirs(jobInstancesLockPath);
        emptyChildCleaner = new ChildReaper(zk.client().client(), jobInstancesLockPath, Reaper.Mode.REAP_INDEFINITELY);
        try {

            String serversFailover = Lock.PREFIX + ZkPaths.SERVER_FAILOVER;
            zk.client().mkdirs(serversFailover);
            emptyChildCleaner.addPath(serversFailover);

            emptyChildCleaner.start();
        } catch (Exception e) {
            throw new ZkException(e);
        }
    }

    @Override
    public void doShutdown() {
        if (emptyChildCleaner != null){
            try {
                emptyChildCleaner.close();
            } catch (IOException e) {

            }
        }
    }

    @Override
    public void destroy() throws Exception {
        shutdown();
    }
}
