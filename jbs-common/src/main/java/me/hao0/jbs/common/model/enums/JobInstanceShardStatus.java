package me.hao0.jbs.common.model.enums;

import java.util.Objects;


public enum JobInstanceShardStatus {


    NEW(0, "job.instance.shard.status.new"),


    RUNNING(1, "job.instance.shard.status.running"),


    SUCCESS(2, "job.instance.shard.status.success"),


    FAILED(3, "job.instance.shard.status.failed");

    private Integer value;

    private String code;

    JobInstanceShardStatus(Integer value, String code){
        this.value = value;
        this.code = code;
    }

    public Integer value(){
        return value;
    }

    public String code(){
        return code;
    }

    public static JobInstanceShardStatus from(Integer status){
        for (JobInstanceShardStatus s : JobInstanceShardStatus.values()){
            if (Objects.equals(s.value, status)){
                return s;
            }
        }
        throw new IllegalStateException("invalid job instance shard status value: " + status);
    }
}
