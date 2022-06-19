package me.hao0.jbs.server.cluster.server;


public interface ServerChangedListener {


    void onServerChanged(String server, Boolean join);
}
