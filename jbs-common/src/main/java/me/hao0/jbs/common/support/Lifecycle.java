package me.hao0.jbs.common.support;


public interface Lifecycle {

    void start();

    boolean isStart();

    void shutdown();

    boolean isShutdown();
}
