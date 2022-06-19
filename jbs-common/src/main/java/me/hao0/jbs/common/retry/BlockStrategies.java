

package me.hao0.jbs.common.retry;

import javax.annotation.concurrent.Immutable;


public final class BlockStrategies {

    private static final BlockStrategy THREAD_SLEEP_STRATEGY = new ThreadSleepStrategy();

    private BlockStrategies() {
    }


    public static BlockStrategy threadSleepStrategy() {
        return THREAD_SLEEP_STRATEGY;
    }

    @Immutable
    private static class ThreadSleepStrategy implements BlockStrategy {

        @Override
        public void block(long sleepTime) throws InterruptedException {
            Thread.sleep(sleepTime);
        }
    }
}