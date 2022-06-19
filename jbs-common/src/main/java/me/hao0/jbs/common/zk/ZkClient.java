package me.hao0.jbs.common.zk;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import me.hao0.jbs.common.exception.ZkException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.EnsurePath;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


public class ZkClient {

    private static final Logger log = LoggerFactory.getLogger(ZkClient.class);

    private static final ExponentialBackoffRetry DEFAULT_RETRY_STRATEGY = new ExponentialBackoffRetry(1000, 3);

    private final String hosts;

    private final String namespace;

    private final ExponentialBackoffRetry retryStrategy;

    private CuratorFramework client;

    private volatile boolean started;

    private final java.util.concurrent.locks.Lock RESTART_LOCK = new ReentrantLock();

    private ZkClient(String hosts, String namespace, ExponentialBackoffRetry retryStrategy){
        this.hosts = hosts;
        this.namespace = namespace;
        this.retryStrategy = retryStrategy;
    }


    public static ZkClient newClient(String hosts, String namespace){
        return newClient(hosts, namespace, DEFAULT_RETRY_STRATEGY);
    }


    public static ZkClient newClient(String hosts, String namespace, ExponentialBackoffRetry retryStrategy){
        ZkClient zc = new ZkClient(hosts, namespace, retryStrategy);
        zc.start();
        return zc;
    }

    private void start() {

        if (started){
            return;
        }

        doStart();
    }

    private void doStart(){

        client = CuratorFrameworkFactory.builder()
                    .connectString(hosts)
                    .namespace(namespace)
                    .retryPolicy(retryStrategy)
                    .build();

        client.start();

        try {

            client.blockUntilConnected(30, TimeUnit.SECONDS);
            started = true;
        } catch (InterruptedException e) {
            throw new ZkException(e);
        }
    }

    public void restart(){

        try {

            boolean locked = RESTART_LOCK.tryLock(30, TimeUnit.SECONDS);
            if (!locked){
                log.warn("timeout to get the restart lock, maybe it's locked by another.");
                return;
            }

            if (client.getZookeeperClient().isConnected()){
                return;
            }

            if (client != null){

                client.close();
            }

            doStart();

        } catch (InterruptedException e) {
            log.error("failed to get the restart lock, cause: {}", Throwables.getStackTraceAsString(e));
        } finally {
            RESTART_LOCK.unlock();
        }

    }


    public CuratorFramework client(){
        return client;
    }


    public void shutdown(){
        if (client != null){
            client.close();
            started = false;
        }
    }


    public String create(String path) {
        return create(path, (byte[])null);
    }


    public String create(String path, byte[] data) {
        try {
            return client.create().withMode(CreateMode.PERSISTENT).forPath(path, data);
        } catch (Exception e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public String create(String path, String data){
        try {
            return create(path, data.getBytes("UTF-8"));
        } catch (Exception e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public String create(String path, Object obj){
        return create(path, JSON.toJSONString(obj));
    }


    public String createSequential(String path, byte[] data) {
        try {
            return client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path, data);
        } catch (Exception e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public String createSequential(String path, String data) {
        try {
            return createSequential(path, data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public String createSequentialJson(String path, Object obj) {
        try {
            return createSequential(path, JSON.toJSONString(obj).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }



    public String createEphemeral(String path) {
        return createEphemeral(path, (byte[]) null);
    }


    public String createEphemeral(String path, byte[] data) {
        try {
            return client.create().withMode(CreateMode.EPHEMERAL).forPath(path, data);
        } catch (Exception e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public String createEphemeral(String path, String data){
        try {
            return client.create().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes("UTF-8"));
        } catch (Exception e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public String createEphemeral(String path, Integer data) {
        return createEphemeral(path, data.toString());
    }


    public String createEphemeral(String path, Object obj) {
        return createEphemeral(path, JSON.toJSONString(obj));
    }


    public String createEphemeralSequential(String path, byte[] data) {
        try {
            return client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, data);
        } catch (Exception e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public String createEphemeralSequential(String path, String data) {
        try {
            return createEphemeralSequential(path, data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public String createEphemeralSequential(String path, Object obj) {
        return createEphemeralSequential(path, JSON.toJSONString(obj));
    }


    public Boolean createIfNotExists(String path, String data) {
        try {
            return createIfNotExists(path, data.getBytes("UTF-8"));
        } catch (Exception e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public Boolean createIfNotExists(String path) {
        return createIfNotExists(path, (byte[])null);
    }


    public Boolean createIfNotExists(String path, byte[] data) {
        try {
            Stat pathStat = client.checkExists().forPath(path);
            if (pathStat == null){
                String nodePath = client.create().forPath(path, data);
                return Strings.isNullOrEmpty(nodePath) ? Boolean.FALSE : Boolean.TRUE;
            }
        } catch (Exception e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }

        return Boolean.FALSE;
    }


    public Boolean checkExists(String path){
        try {
            Stat pathStat = client.checkExists().forPath(path);
            return pathStat != null;
        } catch (Exception e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public Boolean mkdirs(String dir){
        try {
            EnsurePath clientAppPathExist =
                    new EnsurePath("/" + client.getNamespace() + slash(dir));
            clientAppPathExist.ensure(client.getZookeeperClient());
            return Boolean.TRUE;
        } catch (Exception e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }

    public Boolean update(String path, Integer data){
        return update(path, data.toString());
    }

    public Boolean update(String path, Object data){
        return update(path, JSON.toJSONString(data));
    }

    public Boolean update(String path, String data){
        try {
            return update(path, data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new ZkException(e);
        }
    }

    public Boolean update(String path){
        return update(path, (byte[])null);
    }

    public Boolean update(String path, byte[] data){
        try {
            client.setData().forPath(path, data);
            return Boolean.TRUE;
        } catch (Exception e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public void delete(String path) {
        try {
            client.delete().forPath(path);
        } catch (Exception e){
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public void deleteIfExists(String path) {
        try {
            if(checkExists(path)){
                delete(path);
            }
        } catch (Exception e){
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public void deleteRecursively(String path){
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e){
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public void deleteRecursivelyIfExists(String path){
        try {
            if(checkExists(path)){
                deleteRecursively(path);
            }
        } catch (Exception e){
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public byte[] get(String path){
        try {
            return client.getData().forPath(path);
        } catch (Exception e){
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }


    public String getString(String path){
        byte[] data = get(path);
        if (data != null){
            try {
                return new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public Integer getInteger(String nodePath) {
        String nodeValue = getString(nodePath);
        return Strings.isNullOrEmpty(nodeValue) ? null : Integer.parseInt(nodeValue);
    }


    public <T> T getJson(String path, Class<T> clazz){
        byte[] data = get(path);
        if (data != null){
            try {
                String json = new String(data, "UTF-8");
                return JSON.parseObject(json, clazz);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    public List<String> gets(String path){
        try {

            if (!checkExists(path)){
                return Collections.emptyList();
            }

            return client.getChildren().forPath(path);
        } catch (Exception e) {
            handleConnectionLoss(e);
            throw new ZkException(e);
        }
    }

    private String slash(String path){
        return path.startsWith("/") ? path : "/" + path;
    }


    public ChildWatcher newChildWatcher(String path, ChildListener listener) {
        return newChildWatcher(path, listener, Boolean.TRUE);
    }


    public ChildWatcher newChildWatcher(String path, ChildListener listener, Boolean cacheChildData) {
        return new ChildWatcher(client, path, cacheChildData, listener);
    }


    public NodeWatcher newNodeWatcher(String nodePath, NodeListener listener){
        return new NodeWatcher(client, nodePath, listener);
    }


    public NodeWatcher newNodeWatcher(String nodePath){
        return newNodeWatcher(nodePath, null);
    }


    public Lock newLock(String path) {
        return new Lock(client, path);
    }


    public Leader acquireLeader(String leaderPath, LeaderListener listener){
        return acquireLeader(null, leaderPath, listener);
    }


    public Leader acquireLeader(String id, String leaderPath, LeaderListener listener){
        return new Leader(client, id, leaderPath, listener);
    }

    private void handleConnectionLoss(Exception e){
        if (e instanceof KeeperException.ConnectionLossException){

            log.warn("zk client will restart...");


            restart();

            log.warn("zk client do restart finished.");
        }
    }
}