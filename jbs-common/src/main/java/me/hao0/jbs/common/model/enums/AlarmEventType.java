package me.hao0.jbs.common.model.enums;

import java.util.Objects;


public enum AlarmEventType {


    JOB_TIMEOUT(100, "alarm.type.job.timeout"),


    JOB_FAILED(101, "alarm.type.job.failed");

    private Integer value;

    private String code;

    AlarmEventType(Integer value, String code){
        this.value = value;
        this.code = code;
    }

    public Integer value(){
        return value;
    }

    public String code(){
        return code;
    }

    public static AlarmEventType from(Integer value){

        for (AlarmEventType t : AlarmEventType.values()){
            if (Objects.equals(t.value, value)){
                return t;
            }
        }

        throw new IllegalStateException("invalid alarm event type: " + value);
    }
}
