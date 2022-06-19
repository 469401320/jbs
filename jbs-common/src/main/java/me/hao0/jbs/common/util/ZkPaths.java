package me.hao0.jbs.common.util;

import com.google.common.base.Strings;


public final class ZkPaths {


    public static final String DEFAULT_NS = "ats";


    private static final String SLASH = "/";


    public static final String CLUSTER = "/cluster";


    public static final String LEADER = CLUSTER + "/leader";


    public static final String SERVERS = CLUSTER + "/servers";


    public static final String CLIENTS = CLUSTER + "/clients";


    public static final String JOBS = "/jobs";

    public static final String JOB_INSTANCES = "/job_inss";

    public static final String SERVER_FAILOVER = "/servers_failover";


    public static String pathOfServer(String server){
        return format(SERVERS, server);
    }


    public static String pathOfAppClients(String appName){
        return format(CLIENTS, appName);
    }


    public static String pathOfAppClient(String appName, String client){
        return format(CLIENTS, appName, client);
    }


    public static String pathOfJob(String appName, String jobClass){
        return format(JOBS, appName, jobClass);
    }


    public static String pathOfJobState(String appName, String jobClass){
        return format(JOBS, appName, jobClass, "state");
    }


    public static String pathOfJobScheduler(String appName, String jobClass) {
        return format(JOBS, appName, jobClass, "scheduler");
    }


    public static String pathOfJobFireTime(String appName, String jobClass) {
        return format(JOBS, appName, jobClass, "fireTime");
    }


    public static String pathOfJobInstances(String appName, String jobClass){
        return format(JOBS, appName, jobClass, "instances");
    }


    public static String pathOfJobInstance(String appName, String jobClass, Long instanceId){
        return format(JOBS, appName, jobClass, "instances", instanceId);
    }


    public static String pathOfJobInstanceLock(Long jobInstanceId) {
        return format(JOB_INSTANCES, jobInstanceId);
    }


    public static String pathOfServerFailoverLock(String server) {
        return format(SERVER_FAILOVER, server);
    }


    public static String format(Object... parts){
        StringBuilder key = new StringBuilder();
        for (Object part : parts){
            key.append(SLASH).append(part);
        }
        String strKey = key.toString();
        return strKey.startsWith("//") ? strKey.replace("//", "/") : strKey;
    }

    public static String lastNode(String path){
        if (Strings.isNullOrEmpty(path)){
            return null;
        }
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
