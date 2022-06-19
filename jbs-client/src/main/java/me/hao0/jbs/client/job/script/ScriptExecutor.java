package me.hao0.jbs.client.job.script;

import me.hao0.jbs.client.job.JobResult;

import java.util.Map;


public interface ScriptExecutor {


    JobResult exec(String command);


    JobResult exec(String command, Map<String, String> env);
}
