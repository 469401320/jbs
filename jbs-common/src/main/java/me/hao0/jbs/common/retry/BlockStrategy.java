

package me.hao0.jbs.common.retry;


public interface BlockStrategy {


    void block(long sleepTime) throws InterruptedException;
}