package me.hao0.jbs.common.zk;

import me.hao0.jbs.common.exception.ZkException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import java.io.IOException;


public class NodeWatcher {

    private final String path;

    private final NodeCache node;


    NodeWatcher(final CuratorFramework client, final String path, final NodeListener listener) {

        this.path = path;

        node = new NodeCache(client, path);

        if (listener != null) {

            node.getListenable().addListener(new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    ChildData data = node.getCurrentData();
                    if (data != null){
                        listener.onUpdate(data.getData());
                    } else {
                        listener.onDelete();
                    }
                }
            });
        }

        try {
            node.start();
        } catch (Exception e) {
            throw new ZkException(e);
        }
    }

    public String getPath(){
        return path;
    }

    public String getData(){
        ChildData child = node.getCurrentData();
        return child == null ? null : new String(child.getData());
    }

    public void stop(){
        if (node != null){
            try {
                node.close();
            } catch (IOException e) {

            }
        }
    }
}