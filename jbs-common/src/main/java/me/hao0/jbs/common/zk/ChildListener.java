package me.hao0.jbs.common.zk;


public abstract class ChildListener {


    protected void onAdd(String path, byte[] data) {
    }


    protected void onDelete(String path) {
    }


    protected void onUpdate(String path, byte[] newData) {
    }
}