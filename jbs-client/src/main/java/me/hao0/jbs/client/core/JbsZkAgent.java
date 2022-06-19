package me.hao0.jbs.client.core;

import me.hao0.jbs.common.support.Lifecycle;
import me.hao0.jbs.common.support.Component;
import me.hao0.jbs.common.util.ZkPaths;
import me.hao0.jbs.common.zk.ChildListener;
import me.hao0.jbs.common.zk.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;


class JbsZkAgent extends Component implements Lifecycle {

    private static final Logger log = LoggerFactory.getLogger(JbsZkAgent.class);

    private AbstractJbsClient client;


    private ZkClient zk;

    private ClientRegister clientRegister;

    JbsZkAgent(AbstractJbsClient client, String zkServers, String namespace){
        this.client = client;
        this.zk = ZkClient.newClient(zkServers, namespace);
    }

    public ZkClient client(){
        return this.zk;
    }

    @Override
    public void doStart(){


        zk.mkdirs(ZkPaths.pathOfAppClients(client.getAppName()));


        clientRegister = new ClientRegister(client);
        clientRegister.start();


        getServersOnce();


        listenOnServerChanged();
    }

    private void getServersOnce() {
        List<String> servers = zk.gets(ZkPaths.SERVERS);
        if (servers.isEmpty()){
            log.warn("there are no available servers, please check the environment.");
            return;
        }

        for (String server: servers){
            client.addHttpServer(server);
        }
    }

    private void listenOnServerChanged() {
        zk.newChildWatcher(ZkPaths.SERVERS, new ChildListener() {

            @Override
            protected void onAdd(String path, byte[] data) {
                String server = ZkPaths.lastNode(path);
                client.addHttpServer(server);
                log.info("The server({}) joined.", server);
            }

            @Override
            protected void onDelete(String path) {
                String server = ZkPaths.lastNode(path);
                client.removeHttpServer(server);
                log.info("The server({}) left.", server);
            }
        });
    }

    @Override
    public void doShutdown(){
        if (zk != null){
            zk.shutdown();
        }
        clientRegister.shutdown();
    }
}
