package me.hao0.jbs.server.cluster.client;

import com.google.common.collect.Lists;
import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.support.Lifecycle;
import me.hao0.jbs.common.support.Component;
import me.hao0.jbs.common.util.CollectionUtil;
import me.hao0.jbs.common.util.ZkPaths;
import me.hao0.jbs.common.zk.ChildListener;
import me.hao0.jbs.common.zk.ChildWatcher;
import me.hao0.jbs.store.support.JbsZkClient;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class AppClientCluster extends Component implements Lifecycle {

    private final String appName;

    private final Set<String> alives = new HashSet<>();

    private final JbsZkClient zk;

    private ChildWatcher watcher;

    public AppClientCluster(JbsZkClient zk, String appName){

        this.appName = appName;
        this.zk = zk;


        String appClientsPath = ZkPaths.pathOfAppClients(appName);
        zk.client().mkdirs(appClientsPath);
        List<String> clients = zk.client().gets(appClientsPath);
        if (!CollectionUtil.isNullOrEmpty(clients)){
            alives.addAll(clients);
        }
    }


    public List<String> alives() {
        return Lists.newArrayList(alives);
    }

    @Override
    public void doStart() {

        watcher = zk.client().newChildWatcher(ZkPaths.pathOfAppClients(appName), new ChildListener() {
            @Override
            protected void onAdd(String path, byte[] data) {



                if (!started || shutdowned) {
                    return;
                }

                String client = ZkPaths.lastNode(path);
                if (alives.contains(client)){
                    return;
                }

                alives.add(client);
                onClientChanged(appName, client, true);
                Logs.info("The app({})'s client({}) joined.", appName, client);
            }

            @Override
            protected void onDelete(String path) {
                String client = ZkPaths.lastNode(path);
                alives.remove(client);
                onClientChanged(appName, client, false);
                Logs.info("The app({})'s client({}) left.", appName, client);
            }
        });
    }

    @Override
    public void doShutdown() {
        watcher.stop();
    }


    public abstract void onClientChanged(String appName, String client, Boolean join);
}
