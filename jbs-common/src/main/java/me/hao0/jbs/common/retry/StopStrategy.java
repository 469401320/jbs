

package me.hao0.jbs.common.retry;


public interface StopStrategy {


    boolean shouldStop(Attempt failedAttempt);
}
