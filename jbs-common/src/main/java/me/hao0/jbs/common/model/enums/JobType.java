package me.hao0.jbs.common.model.enums;

import java.util.Objects;


public enum JobType {


    DEFAULT(1),


    HTTP(2);

    private Integer value;

    JobType(Integer value){
        this.value = value;
    }

    public Integer value(){
        return value;
    }

    public static JobType from(Integer value){

        for (JobType t : JobType.values()){
            if (Objects.equals(t.value, value)){
                return t;
            }
        }

        throw new IllegalStateException("invalid job type value: " + value);
    }
}
