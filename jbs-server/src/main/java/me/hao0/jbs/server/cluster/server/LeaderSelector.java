package me.hao0.jbs.server.cluster.server;

import com.google.common.base.Objects;
import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.support.Lifecycle;
import me.hao0.jbs.common.support.Component;
import me.hao0.jbs.common.util.ZkPaths;
import me.hao0.jbs.common.zk.Leader;
import me.hao0.jbs.common.zk.LeaderListener;
import me.hao0.jbs.server.cluster.client.ClientFailover;
import me.hao0.jbs.store.support.JbsZkClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@org.springframework.stereotype.Component
public class LeaderSelector extends Component implements Lifecycle, InitializingBean, DisposableBean {

    @Autowired
    private JbsZkClient zk;

    @Autowired
    private ServerHost host;

    @Autowired
    private ServerFailover serverFailover;

    @Autowired
    private ClientFailover clientFailover;

    @Autowired
    private ZkCleaner zkCleaner;

    private Leader leader;

    private ScheduledExecutorService scheduler;

    @Override
    public void doStart(){
        leader = zk.client().acquireLeader(host.get(), ZkPaths.LEADER, new LeaderListener() {
            @Override
            public void isLeader() {

                Logs.info("The server {} become the leader.", host.get());


                clientFailover.start();


                zkCleaner.start();


                zk.client().update(ZkPaths.LEADER, host.get());



                scheduler = Executors.newScheduledThreadPool(1);
                scheduler.scheduleAtFixedRate(new LeaderCheckTask(), 1, 5, TimeUnit.SECONDS);
            }
        });



        serverFailover.start();
    }

    @Override
    public void doShutdown() {

        clientFailover.shutdown();

        serverFailover.shutdown();

        zkCleaner.shutdown();

        leader.release();

        if (scheduler != null){
            scheduler.shutdown();
        }
    }


    public String getLeader(){
        return leader.getLeader();
    }


    public Boolean isLeader(){
        return leader.isLeader();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    public void destroy() throws Exception {
        shutdown();
    }

    private class LeaderCheckTask implements Runnable{

        @Override
        public void run() {
            if (!Objects.equal(host.get(), getLeader())){

                Logs.warn("The server({}) isn't the leader, maybe disconnect unexpectedly.", host.get());


                clientFailover.shutdown();


                zkCleaner.shutdown();


                leader.reaquireLeader();


                scheduler.shutdown();
            }
        }
    }
}
