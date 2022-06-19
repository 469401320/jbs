package me.hao0.jbs.common.model.enums;

import java.util.Objects;


public enum JobState {


    DISABLE(0, "job.state.disable"),


    WAITING(1, "job.state.waiting"),


    RUNNING(2, "job.state.running"),


    STOPPED(3, "job.state.stopped"),


    FAILED(4, "job.state.failed"),


    PAUSED(5, "job.state.paused");

    private Integer value;

    private String code;

    JobState(Integer value, String code){
        this.value = value;
        this.code = code;
    }

    public Integer value(){
        return value;
    }

    public String code(){
        return code;
    }

    public static JobState from(Integer state){
        for (JobState s : JobState.values()){
            if (Objects.equals(s.value, state)){
                return s;
            }
        }
        throw new IllegalStateException("invalid job state value: " + state);
    }

    public static Boolean isScheduling(JobState state) {
        return state == WAITING
                || state == RUNNING
                || state == PAUSED
                || state == FAILED;
    }
}
