package me.hao0.jbs.store.support;

import me.hao0.jbs.common.zk.ZkClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JbsZkClient implements DisposableBean {

    private final ZkClient client;

    private final String zkServers;

    @Autowired
    public JbsZkClient(
        @Value("${jbs.zk.servers:127.0.0.1:2181}") String zkServers,
        @Value("${jbs.zk.namespace:ats}") String zkNamespace){
        this.zkServers = zkServers;
        this.client = ZkClient.newClient(zkServers, zkNamespace);
    }

    public ZkClient client(){
        return client;
    }

    public String zkServers(){
        return zkServers;
    }

    @Override
    public void destroy() throws Exception {
        client.shutdown();
    }
}
