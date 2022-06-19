package me.hao0.jbs.common.retry;

import com.google.common.base.Predicate;
import java.util.concurrent.TimeUnit;


public final class Retryers {

    private Retryers(){}

    private static class RetryersHolder{
        static Retryers INSTANCE = new Retryers();
    }

    public static Retryers get(){
        return RetryersHolder.INSTANCE;
    }


    public <T> Retryer<T> newRetryer(Predicate<T> p){
        return newRetryer(p, 3, -1, null);
    }


    public <T> Retryer<T> newRetryer(Predicate<T> p, int fixWaitSecs){
        return newRetryer(p, fixWaitSecs, -1,  null);
    }


    public <T> Retryer<T> newRetryer(Predicate<T> p, int fixWaitSecs, int attemptTimes, RetryListener retryListener){

        RetryerBuilder<T> builder =  RetryerBuilder.<T>newBuilder()
                .retryIfResult(p)
                .retryIfRuntimeException()
                .withWaitStrategy(WaitStrategies.fixedWait(fixWaitSecs, TimeUnit.SECONDS));


        if (attemptTimes > 0){
            builder.withStopStrategy(StopStrategies.stopAfterAttempt(attemptTimes));
        } else {
            builder.withStopStrategy(StopStrategies.neverStop());
        }


        if (retryListener == null){
            retryListener = new DefaultRetryListener();
        }
        builder.withRetryListener(retryListener);

        return builder.build();
    }
}
