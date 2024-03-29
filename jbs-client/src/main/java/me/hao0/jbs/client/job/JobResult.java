package me.hao0.jbs.client.job;


public final class JobResult {


    public static final JobResult SUCCESS = new JobResult(0);


    public static final JobResult FAIL = new JobResult(1);

    public static final JobResult RETRY = new JobResult(2);


    public static final JobResult LATER = new JobResult(3);

    private int code;


    private String error;

    public JobResult(int code) {
        this.code = code;
    }

    public boolean is(JobResult r){
        return r.code == code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static JobResult failed(String error) {
        JobResult r = new JobResult(JobResult.FAIL.code);
        r.setError(error);
        return r;
    }
}
