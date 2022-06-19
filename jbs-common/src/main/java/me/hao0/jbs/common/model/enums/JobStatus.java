package me.hao0.jbs.common.model.enums;

import java.util.Objects;


public enum JobStatus {


    DISABLE(0),


    ENABLE(1);

    private Integer value;

    JobStatus(Integer value){
        this.value = value;
    }

    public Integer value(){
        return value;
    }

    public static JobStatus from(Integer status){
        for (JobStatus s : JobStatus.values()){
            if (Objects.equals(s.value, status)){
                return s;
            }
        }
        throw new IllegalStateException("invalid job status value: " + status);
    }
}
