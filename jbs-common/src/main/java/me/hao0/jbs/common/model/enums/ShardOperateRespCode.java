package me.hao0.jbs.common.model.enums;

import java.util.Objects;


public enum ShardOperateRespCode {


    INSTANCE_NOT_EXIST(0),


    INSTANCE_IS_FINAL(1),




    SHARD_NO_AVAILABLE(2),


    SHARD_PULL_SUCCESS(3),


    SHARD_PULL_FAILED(4),


    SHARD_PULL_COUNT_EXCEED(5),



    SHARD_NOT_PULLER(6),


    SHARD_FINISH_SUCCESS(7),


    SHARD_FINISH_FAILED(8),


    SHARD_NOT_EXIST(9),


    SHARD_CREATE_FAILED(10),


    SHARD_FINAL(11),




    SHARD_RETURN_SUCCESS(12),


    SHARD_RETURN_FAILED(13),


    IP_NOT_ASSIGNED(14);

    private Integer value;

    ShardOperateRespCode(Integer value){
        this.value = value;
    }

    public Integer value(){
        return value;
    }

    public static ShardOperateRespCode from(Integer value){
        for (ShardOperateRespCode s : ShardOperateRespCode.values()){
            if (Objects.equals(s.value, value)){
                return s;
            }
        }
        throw new IllegalStateException("invalid pull shard status value: " + value);
    }


    public static Boolean needPullAgain(ShardOperateRespCode code){

        return (code == SHARD_NO_AVAILABLE
                || code == SHARD_PULL_FAILED);
    }


    public static Boolean needFinishAgain(ShardOperateRespCode code){
        return code == SHARD_FINISH_FAILED;
    }


    public static Boolean needReturnAgain(ShardOperateRespCode code){
        return code == SHARD_RETURN_FAILED;
    }


    public static Boolean needCleanJobInstance(ShardOperateRespCode code) {
        return code == INSTANCE_IS_FINAL || code == INSTANCE_NOT_EXIST;
    }
}
