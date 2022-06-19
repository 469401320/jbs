package me.hao0.jbs.server.cluster.client;


public interface AppClientChangedListener {


    void onChanged(String appName, String client, Boolean join);
}
