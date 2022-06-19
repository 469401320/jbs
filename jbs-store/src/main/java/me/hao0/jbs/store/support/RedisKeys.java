package me.hao0.jbs.store.support;


import me.hao0.jbs.common.model.enums.JobInstanceShardStatus;


public final class RedisKeys {

    public static final String REDIS_NAMESPACE_PROP = "jbs.redis.namespace";

    public static final String REDIS_NAMESPACE = System.getProperty(REDIS_NAMESPACE_PROP, "ats");

    public static final String KEY_DELIMITER = ":";


    public static final String IDS = "ids";


    public static final String ID_GENERATOR = "idg";


    public static final String APP_INDEX_NAMES = format("apps", "names");


    public static final String JOB_CONFIG_MAPPINGS = format("jobs", "cfg_maps");


    public static final String JOB_SERVER_MAPPINGS = format("jobs", "server_maps");


    public static final String ALARM_EVENT_QUEUE = format("alarm", "eq");


    public static String keyOfIdGenerator(String objectPrefix) {
        return format(objectPrefix, ID_GENERATOR);
    }


    public static String keyOfIds(String objectPrefix) {
        return format(objectPrefix, IDS);
    }


    public static String format(Object... parts){
        StringBuilder key = new StringBuilder(REDIS_NAMESPACE);
        for (Object part : parts){
            key.append(KEY_DELIMITER).append(part);
        }
        return key.toString();
    }


    public static String keyOfAppJobNames(Long appId) {
        return format("apps", appId, "job_names");
    }


    public static String keyOfAppJobs(Long appId) {
        return format("apps", appId, "jobs");
    }


    public static String keyOfJobInstances(Long jobId) {
        return format("jobs", jobId, "inss");
    }


    public static String keyOfAppJobClasses(Long appId) {
        return format("apps", appId, "job_classes");
    }


    public static String keyOfServerJobs(String server) {
        return format("servers", server, "jobs");
    }


    public static String keyOfJobInstanceShards(Long jobInstanceId) {
        return format("job_inss", jobInstanceId, "sds");
    }


    public static String keyOfJobInstanceShardsSet(Long jobInstanceId) {
        return format("job_inss", jobInstanceId, "sds_set");
    }


    public static String keyOfJobInstanceFinishShardsSet(Long jobInstanceId) {
        return format("job_inss", jobInstanceId, "sds_fset");
    }


    public static String keyOfJobInstanceStatusShards(Long jobInstanceId, JobInstanceShardStatus status) {
        return format("job_inss", jobInstanceId, "sds", status.value());
    }


    public static String keyOfClientRunningShards(String client) {
        return format("clients", client, "sds");
    }


    public static String keyOfJobNextJobs(Long jobId) {
        return format("jobs", jobId, "next");
    }


    public static String keyOfJobAssigns(Long jobId) {
        return format("jobs", jobId, "assigns");
    }
}
