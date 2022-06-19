

package me.hao0.jbs.common.retry;

import java.util.concurrent.ExecutionException;


public interface Attempt<V> {


    public V get() throws ExecutionException;


    public boolean hasResult();


    public boolean hasException();


    public V getResult() throws IllegalStateException;


    public Throwable getExceptionCause() throws IllegalStateException;


    public long getAttemptNumber();


    public long getDelaySinceFirstAttempt();
}
