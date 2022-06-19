package me.hao0.jbs.common.model.enums;

import java.util.Objects;


public enum JobInstanceStatus {


    NEW(1, "job.instance.status.new"),


    RUNNING(2, "job.instance.status.running"),


    SUCCESS(3, "job.instance.status.success"),


    FAILED(4, "job.instance.status.failed"),


    TERMINATED(5, "job.instance.status.terminated"),


    TIMEOUT_CLOSED(6, "job.instance.status.timeout");

    private Integer value;

    private String code;

    JobInstanceStatus(Integer value, String code){
        this.value = value;
        this.code = code;
    }

    public Integer value(){
        return value;
    }

    public String code(){
        return code;
    }

    public static JobInstanceStatus from(Integer status){
        for (JobInstanceStatus s : JobInstanceStatus.values()){
            if (Objects.equals(s.value, status)){
                return s;
            }
        }
        throw new IllegalStateException("invalid job instance status value: " + status);
    }

    public static boolean isFinal(Integer status) {
        JobInstanceStatus instanceStatus = from(status);
        return instanceStatus == SUCCESS
                || instanceStatus == FAILED
                || instanceStatus == TERMINATED
                || instanceStatus == TIMEOUT_CLOSED;
    }
}
