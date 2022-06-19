package me.hao0.jbs.common.zk;


public abstract class NodeListener {


    public void onUpdate(byte[] newData){}


    public void onDelete(){}
}