package me.hao0.jbs.common.dto;

import java.io.Serializable;


public class SaveNextJob implements Serializable {

    private static final long serialVersionUID = 299727901934803683L;


    private Long nextJobId;

    public Long getNextJobId() {
        return nextJobId;
    }

    public void setNextJobId(Long nextJobId) {
        this.nextJobId = nextJobId;
    }

    @Override
    public String toString() {
        return "SaveNextJob{" +
                "nextJobId=" + nextJobId +
                '}';
    }
}
