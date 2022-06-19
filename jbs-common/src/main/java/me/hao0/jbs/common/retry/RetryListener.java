

package me.hao0.jbs.common.retry;

import com.google.common.annotations.Beta;


@Beta
public interface RetryListener {


    <V> void onRetry(Attempt<V> attempt);
}
