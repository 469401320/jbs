package me.hao0.jbs.common.dto;

import java.io.Serializable;


public class JobAssignSaveDto implements Serializable {

    private static final long serialVersionUID = 4263372541949069063L;

    private String assignIps;

    public String getAssignIps() {
        return assignIps;
    }

    public void setAssignIps(String assignIps) {
        this.assignIps = assignIps;
    }

    @Override
    public String toString() {
        return "JobAssignSaveDto{" +
                "assignIps='" + assignIps + '\'' +
                '}';
    }
}
