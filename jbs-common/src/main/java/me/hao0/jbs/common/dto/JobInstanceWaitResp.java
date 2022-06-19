package me.hao0.jbs.common.dto;


public class JobInstanceWaitResp {


    private boolean success;


    private boolean timeout;

    private JobInstanceWaitResp(){}

    public static JobInstanceWaitResp success(){
        JobInstanceWaitResp r = new JobInstanceWaitResp();
        r.success = true;
        return r;
    }

    public static JobInstanceWaitResp timeout(){
        JobInstanceWaitResp r = new JobInstanceWaitResp();
        r.timeout = true;
        return r;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isTimeout() {
        return timeout;
    }
}
