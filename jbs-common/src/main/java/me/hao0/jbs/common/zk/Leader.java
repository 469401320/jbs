package me.hao0.jbs.common.zk;

import me.hao0.jbs.common.exception.ZkException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.leader.Participant;
import java.util.concurrent.CountDownLatch;


public class Leader {

    private final LeaderSelector selector;

    private CountDownLatch latch;

    Leader(CuratorFramework client, String leaderPath, final LeaderListener listener) {
        this(client, null, leaderPath, listener);
    }

    Leader(CuratorFramework client, String id, String leaderPath, final LeaderListener listener) {
        selector = new LeaderSelector(client, leaderPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {

                latch = new CountDownLatch(1);

                listener.isLeader();


                latch.await();
            }
        });

        if (id != null){
            selector.setId(id);
        }

        selector.start();
    }


    public Boolean isLeader(){
        return selector.hasLeadership();
    }


    public String getLeader(){
        try {
            Participant p = selector.getLeader();
            if (p != null){
                return p.getId();
            }
        } catch (Exception e) {
            throw new ZkException(e);
        }
        return null;
    }


    public Boolean reaquireLeader(){
        return selector.requeue();
    }


    public void release(){
        if (latch != null){
            latch.countDown();
        }
    }
}
