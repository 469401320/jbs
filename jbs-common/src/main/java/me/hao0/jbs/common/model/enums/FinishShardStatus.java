package me.hao0.jbs.common.model.enums;

import java.util.Objects;


public enum FinishShardStatus {


    INSTANCE_NOT_EXIST(0),


    INSTANCE_FINISH(1),


    NOT_OWNER(2),


    FINISH_SUCCESS(3),


    FINISH_FAILED(4),


    SHARD_NOT_EXIST(6);

    private Integer value;

    FinishShardStatus(Integer value){
        this.value = value;
    }

    public Integer value(){
        return value;
    }

    public static FinishShardStatus from(Integer value){
        for (FinishShardStatus s : FinishShardStatus.values()){
            if (Objects.equals(s.value, value)){
                return s;
            }
        }
        throw new IllegalStateException("invalid pull shard status value: " + value);
    }


    public static Boolean needFinish(Integer value){
        FinishShardStatus status = from(value);
        return status == FINISH_FAILED;
    }
}
