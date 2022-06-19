package me.hao0.jbs.common.dto;

import java.io.Serializable;


public class JobFireTime implements Serializable {

    private static final long serialVersionUID = 4612715888992171290L;


    private String current;


    private String prev;


    private String next;

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "JobFireTime{" +
                "current='" + current + '\'' +
                ", prev='" + prev + '\'' +
                ", next='" + next + '\'' +
                '}';
    }
}
