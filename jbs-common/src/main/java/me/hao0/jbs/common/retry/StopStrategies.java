

package me.hao0.jbs.common.retry;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.concurrent.TimeUnit;


public final class StopStrategies {
    private static final StopStrategy NEVER_STOP = new NeverStopStrategy();

    private StopStrategies() {
    }


    public static StopStrategy neverStop() {
        return NEVER_STOP;
    }


    public static StopStrategy stopAfterAttempt(int attemptNumber) {
        return new StopAfterAttemptStrategy(attemptNumber);
    }


    @Deprecated
    public static StopStrategy stopAfterDelay(long delayInMillis) {
        return stopAfterDelay(delayInMillis, TimeUnit.MILLISECONDS);
    }


    public static StopStrategy stopAfterDelay(long duration, @Nonnull TimeUnit timeUnit) {
        Preconditions.checkNotNull(timeUnit, "The time unit may not be null");
        return new StopAfterDelayStrategy(timeUnit.toMillis(duration));
    }

    @Immutable
    private static final class NeverStopStrategy implements StopStrategy {
        @Override
        public boolean shouldStop(Attempt failedAttempt) {
            return false;
        }
    }

    @Immutable
    private static final class StopAfterAttemptStrategy implements StopStrategy {
        private final int maxAttemptNumber;

        public StopAfterAttemptStrategy(int maxAttemptNumber) {
            Preconditions.checkArgument(maxAttemptNumber >= 1, "maxAttemptNumber must be >= 1 but is %d", maxAttemptNumber);
            this.maxAttemptNumber = maxAttemptNumber;
        }

        @Override
        public boolean shouldStop(Attempt failedAttempt) {
            return failedAttempt.getAttemptNumber() >= maxAttemptNumber;
        }
    }

    @Immutable
    private static final class StopAfterDelayStrategy implements StopStrategy {
        private final long maxDelay;

        public StopAfterDelayStrategy(long maxDelay) {
            Preconditions.checkArgument(maxDelay >= 0L, "maxDelay must be >= 0 but is %d", maxDelay);
            this.maxDelay = maxDelay;
        }

        @Override
        public boolean shouldStop(Attempt failedAttempt) {
            return failedAttempt.getDelaySinceFirstAttempt() >= maxDelay;
        }
    }
}
