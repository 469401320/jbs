package me.hao0.jbs.common.zk;

import com.google.common.collect.Lists;
import me.hao0.jbs.common.exception.ZkException;
import me.hao0.jbs.common.util.CollectionUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class ChildWatcher {

    private final String path;

    private final PathChildrenCache cacher;


    ChildWatcher(CuratorFramework client, String path, final Boolean cacheData, final ChildListener listener) {
        this.path = path;
        this.cacher = new PathChildrenCache(client, path, cacheData);

        if (listener != null){
            this.cacher.getListenable().addListener(new PathChildrenCacheListener() {
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                    PathChildrenCacheEvent.Type eventType = event.getType();
                    ChildData childData = event.getData();
                    if (childData == null){
                        return;
                    }
                    String path = childData.getPath();

                    switch (eventType) {
                        case CHILD_ADDED:
                            listener.onAdd(path, childData.getData());
                            break;
                        case CHILD_REMOVED:
                            listener.onDelete(path);
                            break;
                        case CHILD_UPDATED:
                            listener.onUpdate(path, childData.getData());
                            break;
                        case CONNECTION_RECONNECTED:
                            cacher.rebuild();
                        default:
                            break;
                    }
                }
            });
        }

        try {
            this.cacher.start();
        } catch (Exception e) {
            throw new ZkException(e);
        }
    }

    public String getPath(){
        return path;
    }

    public List<String> getDatas(){
        List<ChildData> datas = cacher.getCurrentData();
        if (CollectionUtil.isNullOrEmpty(datas)){
            return Collections.emptyList();
        }

        List<String> stringDatas = Lists.newArrayListWithExpectedSize(datas.size());
        for (ChildData data : datas){
            stringDatas.add(new String(data.getData()));
        }
        return stringDatas;
    }

    public void stop(){
        if (cacher != null){
            try {
                cacher.close();
            } catch (IOException e) {

            }
        }
    }
}