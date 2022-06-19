package me.hao0.jbs.common.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import java.util.concurrent.TimeUnit;


public class Lock {

    public static final String PREFIX = "/locks";

    private InterProcessMutex mutex;

    private Boolean locked = Boolean.FALSE;

    Lock(CuratorFramework client, String path) {
        this.mutex = new InterProcessMutex(client, PREFIX + path);
    }


    public void lock() {
        try {
            mutex.acquire();
            locked = Boolean.TRUE;
        } catch (Exception e) {
            locked = Boolean.FALSE;
        }
    }


    public Boolean lock(long timeout) {
        try {
            locked = mutex.acquire(timeout, TimeUnit.MILLISECONDS);
            return locked;
        } catch (Exception e) {
            locked = Boolean.FALSE;
        }
        return Boolean.FALSE;
    }


    public void unlock() {
        if (locked) {
            try {
                mutex.release();
            } catch (Exception e) {
                throw new RuntimeException("failed to unlock: " + mutex);
            }
        }
    }
}