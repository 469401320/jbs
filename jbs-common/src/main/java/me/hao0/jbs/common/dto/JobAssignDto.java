package me.hao0.jbs.common.dto;

import java.io.Serializable;
import java.util.Set;


public class JobAssignDto implements Serializable {

    private static final long serialVersionUID = 4178691834316148023L;


    private String ip;


    private Boolean assign;


    private Set<String> processes;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getAssign() {
        return assign;
    }

    public void setAssign(Boolean assign) {
        this.assign = assign;
    }

    public Set<String> getProcesses() {
        return processes;
    }

    public void setProcesses(Set<String> processes) {
        this.processes = processes;
    }

    @Override
    public String toString() {
        return "JobAssignDto{" +
                "ip='" + ip + '\'' +
                ", assign=" + assign +
                ", processes=" + processes +
                '}';
    }
}
