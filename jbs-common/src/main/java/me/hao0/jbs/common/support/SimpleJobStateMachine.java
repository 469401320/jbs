package me.hao0.jbs.common.support;

import me.hao0.jbs.common.model.enums.JobState;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class SimpleJobStateMachine {


    private final Map<JobState, Set<JobState>> states = new HashMap<>();

    private SimpleJobStateMachine(){

        configure(JobState.WAITING, JobState.RUNNING);

        configure(JobState.PAUSED, JobState.WAITING);
        configure(JobState.RUNNING, JobState.WAITING);
        configure(JobState.WAITING, JobState.WAITING);

        configure(JobState.WAITING, JobState.PAUSED);
        configure(JobState.RUNNING, JobState.PAUSED);
        configure(JobState.FAILED, JobState.PAUSED);

        configure(JobState.PAUSED, JobState.STOPPED);
        configure(JobState.WAITING, JobState.STOPPED);
        configure(JobState.RUNNING, JobState.STOPPED);
        configure(JobState.FAILED, JobState.STOPPED);
        configure(JobState.STOPPED, JobState.STOPPED);


        configure(JobState.WAITING, JobState.FAILED);
        configure(JobState.RUNNING, JobState.FAILED);

    }


    private void configure(JobState prev, JobState next){
        Set<JobState> previousStates = states.get(next);
        if (previousStates == null){
            previousStates = new HashSet<>();
            states.put(next, previousStates);
        }
        previousStates.add(prev);
    }


    public Boolean allow(JobState current, JobState target){
        Set<JobState> allows = states.get(target);
        if (allows == null || allows.isEmpty()){
            return Boolean.FALSE;
        }
        return allows.contains(current);
    }

    private static class SimpleJobStateMachineHolder{
        static SimpleJobStateMachine INSTANCE = new SimpleJobStateMachine();
    }

    public static SimpleJobStateMachine get(){
        return SimpleJobStateMachineHolder.INSTANCE;
    }


}
