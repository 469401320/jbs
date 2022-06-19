

package me.hao0.jbs.common.retry;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public final class Retryer<V> {
    private final StopStrategy stopStrategy;
    private final WaitStrategy waitStrategy;
    private final BlockStrategy blockStrategy;
    private final AttemptTimeLimiter<V> attemptTimeLimiter;
    private final Predicate<Attempt<V>> rejectionPredicate;
    private final Collection<RetryListener> listeners;


    public Retryer(@Nonnull StopStrategy stopStrategy,
                   @Nonnull WaitStrategy waitStrategy,
                   @Nonnull Predicate<Attempt<V>> rejectionPredicate) {

        this(AttemptTimeLimiters.<V>noTimeLimit(), stopStrategy, waitStrategy, BlockStrategies.threadSleepStrategy(), rejectionPredicate);
    }


    public Retryer(@Nonnull AttemptTimeLimiter<V> attemptTimeLimiter,
                   @Nonnull StopStrategy stopStrategy,
                   @Nonnull WaitStrategy waitStrategy,
                   @Nonnull Predicate<Attempt<V>> rejectionPredicate) {
        this(attemptTimeLimiter, stopStrategy, waitStrategy, BlockStrategies.threadSleepStrategy(), rejectionPredicate);
    }


    public Retryer(@Nonnull AttemptTimeLimiter<V> attemptTimeLimiter,
                   @Nonnull StopStrategy stopStrategy,
                   @Nonnull WaitStrategy waitStrategy,
                   @Nonnull BlockStrategy blockStrategy,
                   @Nonnull Predicate<Attempt<V>> rejectionPredicate) {
        this(attemptTimeLimiter, stopStrategy, waitStrategy, blockStrategy, rejectionPredicate, new ArrayList<RetryListener>());
    }


    @Beta
    public Retryer(@Nonnull AttemptTimeLimiter<V> attemptTimeLimiter,
                   @Nonnull StopStrategy stopStrategy,
                   @Nonnull WaitStrategy waitStrategy,
                   @Nonnull BlockStrategy blockStrategy,
                   @Nonnull Predicate<Attempt<V>> rejectionPredicate,
                   @Nonnull Collection<RetryListener> listeners) {
        Preconditions.checkNotNull(attemptTimeLimiter, "timeLimiter may not be null");
        Preconditions.checkNotNull(stopStrategy, "stopStrategy may not be null");
        Preconditions.checkNotNull(waitStrategy, "waitStrategy may not be null");
        Preconditions.checkNotNull(blockStrategy, "blockStrategy may not be null");
        Preconditions.checkNotNull(rejectionPredicate, "rejectionPredicate may not be null");
        Preconditions.checkNotNull(listeners, "listeners may not null");

        this.attemptTimeLimiter = attemptTimeLimiter;
        this.stopStrategy = stopStrategy;
        this.waitStrategy = waitStrategy;
        this.blockStrategy = blockStrategy;
        this.rejectionPredicate = rejectionPredicate;
        this.listeners = listeners;
    }


    public V call(Callable<V> callable) throws ExecutionException, RetryException {
        long startTime = System.nanoTime();
        for (int attemptNumber = 1; ; attemptNumber++) {
            Attempt<V> attempt;
            try {
                V result = attemptTimeLimiter.call(callable);
                attempt = new ResultAttempt<V>(result, attemptNumber, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
            } catch (Throwable t) {
                attempt = new ExceptionAttempt<V>(t, attemptNumber, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
            }

            for (RetryListener listener : listeners) {
                listener.onRetry(attempt);
            }

            if (!rejectionPredicate.apply(attempt)) {
                return attempt.get();
            }
            if (stopStrategy.shouldStop(attempt)) {
                throw new RetryException(attemptNumber, attempt);
            } else {
                long sleepTime = waitStrategy.computeSleepTime(attempt);
                try {
                    blockStrategy.block(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RetryException(attemptNumber, attempt);
                }
            }
        }
    }


    public RetryerCallable<V> wrap(Callable<V> callable) {
        return new RetryerCallable<V>(this, callable);
    }

    @Immutable
    static final class ResultAttempt<R> implements Attempt<R> {
        private final R result;
        private final long attemptNumber;
        private final long delaySinceFirstAttempt;

        public ResultAttempt(R result, long attemptNumber, long delaySinceFirstAttempt) {
            this.result = result;
            this.attemptNumber = attemptNumber;
            this.delaySinceFirstAttempt = delaySinceFirstAttempt;
        }

        @Override
        public R get() throws ExecutionException {
            return result;
        }

        @Override
        public boolean hasResult() {
            return true;
        }

        @Override
        public boolean hasException() {
            return false;
        }

        @Override
        public R getResult() throws IllegalStateException {
            return result;
        }

        @Override
        public Throwable getExceptionCause() throws IllegalStateException {
            throw new IllegalStateException("The attempt resulted in a result, not in an exception");
        }

        @Override
        public long getAttemptNumber() {
            return attemptNumber;
        }

        @Override
        public long getDelaySinceFirstAttempt() {
            return delaySinceFirstAttempt;
        }
    }

    @Immutable
    static final class ExceptionAttempt<R> implements Attempt<R> {
        private final ExecutionException e;
        private final long attemptNumber;
        private final long delaySinceFirstAttempt;

        public ExceptionAttempt(Throwable cause, long attemptNumber, long delaySinceFirstAttempt) {
            this.e = new ExecutionException(cause);
            this.attemptNumber = attemptNumber;
            this.delaySinceFirstAttempt = delaySinceFirstAttempt;
        }

        @Override
        public R get() throws ExecutionException {
            throw e;
        }

        @Override
        public boolean hasResult() {
            return false;
        }

        @Override
        public boolean hasException() {
            return true;
        }

        @Override
        public R getResult() throws IllegalStateException {
            throw new IllegalStateException("The attempt resulted in an exception, not in a result");
        }

        @Override
        public Throwable getExceptionCause() throws IllegalStateException {
            return e.getCause();
        }

        @Override
        public long getAttemptNumber() {
            return attemptNumber;
        }

        @Override
        public long getDelaySinceFirstAttempt() {
            return delaySinceFirstAttempt;
        }
    }


    public static class RetryerCallable<X> implements Callable<X> {
        private Retryer<X> retryer;
        private Callable<X> callable;

        private RetryerCallable(Retryer<X> retryer,
                                Callable<X> callable) {
            this.retryer = retryer;
            this.callable = callable;
        }


        @Override
        public X call() throws ExecutionException, RetryException {
            return retryer.call(callable);
        }
    }
}
