package me.hao0.jbs.common.dto;

import java.io.Serializable;


public class ClientInfo implements Serializable {

    private static final long serialVersionUID = -3395261718901387010L;

    private String addr;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "addr='" + addr + '\'' +
                '}';
    }
}

