

package me.hao0.jbs.common.retry;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


public class AttemptTimeLimiters {

    private AttemptTimeLimiters() {
    }


    public static <V> AttemptTimeLimiter<V> noTimeLimit() {
        return new NoAttemptTimeLimit<V>();
    }


    public static <V> AttemptTimeLimiter<V> fixedTimeLimit(long duration, @Nonnull TimeUnit timeUnit) {
        Preconditions.checkNotNull(timeUnit);
        return new FixedAttemptTimeLimit<V>(duration, timeUnit);
    }


    public static <V> AttemptTimeLimiter<V> fixedTimeLimit(long duration, @Nonnull TimeUnit timeUnit, @Nonnull ExecutorService executorService) {
        Preconditions.checkNotNull(timeUnit);
        return new FixedAttemptTimeLimit<V>(duration, timeUnit, executorService);
    }

    @Immutable
    private static final class NoAttemptTimeLimit<V> implements AttemptTimeLimiter<V> {
        @Override
        public V call(Callable<V> callable) throws Exception {
            return callable.call();
        }
    }

    @Immutable
    private static final class FixedAttemptTimeLimit<V> implements AttemptTimeLimiter<V> {

        private final TimeLimiter timeLimiter;
        private final long duration;
        private final TimeUnit timeUnit;

        public FixedAttemptTimeLimit(long duration, @Nonnull TimeUnit timeUnit) {
            this(new SimpleTimeLimiter(), duration, timeUnit);
        }

        public FixedAttemptTimeLimit(long duration, @Nonnull TimeUnit timeUnit, @Nonnull ExecutorService executorService) {
            this(new SimpleTimeLimiter(executorService), duration, timeUnit);
        }

        private FixedAttemptTimeLimit(@Nonnull TimeLimiter timeLimiter, long duration, @Nonnull TimeUnit timeUnit) {
            Preconditions.checkNotNull(timeLimiter);
            Preconditions.checkNotNull(timeUnit);
            this.timeLimiter = timeLimiter;
            this.duration = duration;
            this.timeUnit = timeUnit;
        }

        @Override
        public V call(Callable<V> callable) throws Exception {
            return timeLimiter.callWithTimeout(callable, duration, timeUnit, true);
        }
    }
}
