package me.hao0.jbs.common.model.enums;

import java.util.Objects;


public enum AlarmNotifyType {


    EMAIL(1, "EMAIL"),


    SMS(2, "SMS"),


    MOBILE(4, "MOBILE"),


    WECHAT(8, "WECHAT");

    private Integer value;

    private String code;

    AlarmNotifyType(Integer value, String code){
        this.value = value;
        this.code = code;
    }

    public Integer value(){
        return value;
    }

    public String code(){
        return code;
    }

    public static AlarmNotifyType from(Integer value){

        for (AlarmNotifyType t : AlarmNotifyType.values()){
            if (Objects.equals(t.value, value)){
                return t;
            }
        }

        throw new IllegalStateException("invalid alarm notify type: " + value);
    }
}
