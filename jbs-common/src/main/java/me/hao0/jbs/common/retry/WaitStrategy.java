

package me.hao0.jbs.common.retry;


public interface WaitStrategy {


    long computeSleepTime(Attempt failedAttempt);
}
