package me.hao0.jbs.common.model.enums;

import java.util.Objects;


public enum JobTriggerType {


    DEFAULT(1, "job.trigger.type.default"),


    API(2, "job.trigger.type.api"),


    NOTIFY(3, "job.trigger.type.notify");

    private Integer value;

    private String code;

    JobTriggerType(Integer value, String code){
        this.value = value;
        this.code = code;
    }

    public Integer value(){
        return value;
    }

    public String code(){
        return code;
    }

    public static JobTriggerType from(Integer value){

        for (JobTriggerType t : JobTriggerType.values()){
            if (Objects.equals(t.value, value)){
                return t;
            }
        }

        throw new IllegalStateException("invalid job trigger type value: " + value);
    }
}
