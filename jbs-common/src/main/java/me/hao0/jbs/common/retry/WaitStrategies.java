

package me.hao0.jbs.common.retry;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public final class WaitStrategies {

    private static final WaitStrategy NO_WAIT_STRATEGY = new FixedWaitStrategy(0L);

    private WaitStrategies() {
    }


    public static WaitStrategy noWait() {
        return NO_WAIT_STRATEGY;
    }


    public static WaitStrategy fixedWait(long sleepTime, @Nonnull TimeUnit timeUnit) throws IllegalStateException {
        Preconditions.checkNotNull(timeUnit, "The time unit may not be null");
        return new FixedWaitStrategy(timeUnit.toMillis(sleepTime));
    }


    public static WaitStrategy randomWait(long maximumTime, @Nonnull TimeUnit timeUnit) {
        Preconditions.checkNotNull(timeUnit, "The time unit may not be null");
        return new RandomWaitStrategy(0L, timeUnit.toMillis(maximumTime));
    }


    public static WaitStrategy randomWait(long minimumTime,
                                          @Nonnull TimeUnit minimumTimeUnit,
                                          long maximumTime,
                                          @Nonnull TimeUnit maximumTimeUnit) {
        Preconditions.checkNotNull(minimumTimeUnit, "The minimum time unit may not be null");
        Preconditions.checkNotNull(maximumTimeUnit, "The maximum time unit may not be null");
        return new RandomWaitStrategy(minimumTimeUnit.toMillis(minimumTime),
                maximumTimeUnit.toMillis(maximumTime));
    }


    public static WaitStrategy incrementingWait(long initialSleepTime,
                                                @Nonnull TimeUnit initialSleepTimeUnit,
                                                long increment,
                                                @Nonnull TimeUnit incrementTimeUnit) {
        Preconditions.checkNotNull(initialSleepTimeUnit, "The initial sleep time unit may not be null");
        Preconditions.checkNotNull(incrementTimeUnit, "The increment time unit may not be null");
        return new IncrementingWaitStrategy(initialSleepTimeUnit.toMillis(initialSleepTime),
                incrementTimeUnit.toMillis(increment));
    }


    public static WaitStrategy exponentialWait() {
        return new ExponentialWaitStrategy(1, Long.MAX_VALUE);
    }


    public static WaitStrategy exponentialWait(long maximumTime,
                                               @Nonnull TimeUnit maximumTimeUnit) {
        Preconditions.checkNotNull(maximumTimeUnit, "The maximum time unit may not be null");
        return new ExponentialWaitStrategy(1, maximumTimeUnit.toMillis(maximumTime));
    }


    public static WaitStrategy exponentialWait(long multiplier,
                                               long maximumTime,
                                               @Nonnull TimeUnit maximumTimeUnit) {
        Preconditions.checkNotNull(maximumTimeUnit, "The maximum time unit may not be null");
        return new ExponentialWaitStrategy(multiplier, maximumTimeUnit.toMillis(maximumTime));
    }


    public static WaitStrategy fibonacciWait() {
        return new FibonacciWaitStrategy(1, Long.MAX_VALUE);
    }


    public static WaitStrategy fibonacciWait(long maximumTime,
                                             @Nonnull TimeUnit maximumTimeUnit) {
        Preconditions.checkNotNull(maximumTimeUnit, "The maximum time unit may not be null");
        return new FibonacciWaitStrategy(1, maximumTimeUnit.toMillis(maximumTime));
    }


    public static WaitStrategy fibonacciWait(long multiplier,
                                             long maximumTime,
                                             @Nonnull TimeUnit maximumTimeUnit) {
        Preconditions.checkNotNull(maximumTimeUnit, "The maximum time unit may not be null");
        return new FibonacciWaitStrategy(multiplier, maximumTimeUnit.toMillis(maximumTime));
    }


    public static <T extends Throwable> WaitStrategy exceptionWait(@Nonnull Class<T> exceptionClass,
                                                                   @Nonnull Function<T, Long> function) {
        Preconditions.checkNotNull(exceptionClass, "exceptionClass may not be null");
        Preconditions.checkNotNull(function, "function may not be null");
        return new ExceptionWaitStrategy<T>(exceptionClass, function);
    }


    public static WaitStrategy join(WaitStrategy... waitStrategies) {
        Preconditions.checkState(waitStrategies.length > 0, "Must have at least one wait strategy");
        List<WaitStrategy> waitStrategyList = Lists.newArrayList(waitStrategies);
        Preconditions.checkState(!waitStrategyList.contains(null), "Cannot have a null wait strategy");
        return new CompositeWaitStrategy(waitStrategyList);
    }

    @Immutable
    private static final class FixedWaitStrategy implements WaitStrategy {
        private final long sleepTime;

        public FixedWaitStrategy(long sleepTime) {
            Preconditions.checkArgument(sleepTime >= 0L, "sleepTime must be >= 0 but is %d", sleepTime);
            this.sleepTime = sleepTime;
        }

        @Override
        public long computeSleepTime(Attempt failedAttempt) {
            return sleepTime;
        }
    }

    @Immutable
    private static final class RandomWaitStrategy implements WaitStrategy {
        private static final Random RANDOM = new Random();
        private final long minimum;
        private final long maximum;

        public RandomWaitStrategy(long minimum, long maximum) {
            Preconditions.checkArgument(minimum >= 0, "minimum must be >= 0 but is %d", minimum);
            Preconditions.checkArgument(maximum > minimum, "maximum must be > minimum but maximum is %d and minimum is", maximum, minimum);

            this.minimum = minimum;
            this.maximum = maximum;
        }

        @Override
        public long computeSleepTime(Attempt failedAttempt) {
            long t = Math.abs(RANDOM.nextLong()) % (maximum - minimum);
            return t + minimum;
        }
    }

    @Immutable
    private static final class IncrementingWaitStrategy implements WaitStrategy {
        private final long initialSleepTime;
        private final long increment;

        public IncrementingWaitStrategy(long initialSleepTime,
                                        long increment) {
            Preconditions.checkArgument(initialSleepTime >= 0L, "initialSleepTime must be >= 0 but is %d", initialSleepTime);
            this.initialSleepTime = initialSleepTime;
            this.increment = increment;
        }

        @Override
        public long computeSleepTime(Attempt failedAttempt) {
            long result = initialSleepTime + (increment * (failedAttempt.getAttemptNumber() - 1));
            return result >= 0L ? result : 0L;
        }
    }

    @Immutable
    private static final class ExponentialWaitStrategy implements WaitStrategy {
        private final long multiplier;
        private final long maximumWait;

        public ExponentialWaitStrategy(long multiplier,
                                       long maximumWait) {
            Preconditions.checkArgument(multiplier > 0L, "multiplier must be > 0 but is %d", multiplier);
            Preconditions.checkArgument(maximumWait >= 0L, "maximumWait must be >= 0 but is %d", maximumWait);
            Preconditions.checkArgument(multiplier < maximumWait, "multiplier must be < maximumWait but is %d", multiplier);
            this.multiplier = multiplier;
            this.maximumWait = maximumWait;
        }

        @Override
        public long computeSleepTime(Attempt failedAttempt) {
            double exp = Math.pow(2, failedAttempt.getAttemptNumber());
            long result = Math.round(multiplier * exp);
            if (result > maximumWait) {
                result = maximumWait;
            }
            return result >= 0L ? result : 0L;
        }
    }

    @Immutable
    private static final class FibonacciWaitStrategy implements WaitStrategy {
        private final long multiplier;
        private final long maximumWait;

        public FibonacciWaitStrategy(long multiplier, long maximumWait) {
            Preconditions.checkArgument(multiplier > 0L, "multiplier must be > 0 but is %d", multiplier);
            Preconditions.checkArgument(maximumWait >= 0L, "maximumWait must be >= 0 but is %d", maximumWait);
            Preconditions.checkArgument(multiplier < maximumWait, "multiplier must be < maximumWait but is %d", multiplier);
            this.multiplier = multiplier;
            this.maximumWait = maximumWait;
        }

        @Override
        public long computeSleepTime(Attempt failedAttempt) {
            long fib = fib(failedAttempt.getAttemptNumber());
            long result = multiplier * fib;

            if (result > maximumWait || result < 0L) {
                result = maximumWait;
            }

            return result >= 0L ? result : 0L;
        }

        private long fib(long n) {
            if (n == 0L) return 0L;
            if (n == 1L) return 1L;

            long prevPrev = 0L;
            long prev = 1L;
            long result = 0L;

            for (long i = 2L; i <= n; i++) {
                result = prev + prevPrev;
                prevPrev = prev;
                prev = result;
            }

            return result;
        }
    }

    @Immutable
    private static final class CompositeWaitStrategy implements WaitStrategy {
        private final List<WaitStrategy> waitStrategies;

        public CompositeWaitStrategy(List<WaitStrategy> waitStrategies) {
            Preconditions.checkState(!waitStrategies.isEmpty(), "Need at least one wait strategy");
            this.waitStrategies = waitStrategies;
        }

        @Override
        public long computeSleepTime(Attempt failedAttempt) {
            long waitTime = 0L;
            for (WaitStrategy waitStrategy : waitStrategies) {
                waitTime += waitStrategy.computeSleepTime(failedAttempt);
            }
            return waitTime;
        }
    }

    @Immutable
    private static final class ExceptionWaitStrategy<T extends Throwable> implements WaitStrategy {
        private final Class<T> exceptionClass;
        private final Function<T, Long> function;

        public ExceptionWaitStrategy(@Nonnull Class<T> exceptionClass, @Nonnull Function<T, Long> function) {
            this.exceptionClass = exceptionClass;
            this.function = function;
        }

        @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions", "unchecked"})
        @Override
        public long computeSleepTime(Attempt lastAttempt) {
            if (lastAttempt.hasException()) {
                Throwable cause = lastAttempt.getExceptionCause();
                if (exceptionClass.isAssignableFrom(cause.getClass())) {
                    return function.apply((T) cause);
                }
            }
            return 0L;
        }
    }
}
