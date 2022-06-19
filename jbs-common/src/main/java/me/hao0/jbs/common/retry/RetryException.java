

package me.hao0.jbs.common.retry;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkNotNull;


@Immutable
public final class RetryException extends Exception {

    private final int numberOfFailedAttempts;
    private final Attempt<?> lastFailedAttempt;


    public RetryException(int numberOfFailedAttempts, @Nonnull Attempt<?> lastFailedAttempt) {
        this("Retrying failed to complete successfully after " + numberOfFailedAttempts + " attempts.", numberOfFailedAttempts, lastFailedAttempt);
    }


    public RetryException(String message, int numberOfFailedAttempts, Attempt<?> lastFailedAttempt) {
        super(message, checkNotNull(lastFailedAttempt, "Last attempt was null").hasException() ? lastFailedAttempt.getExceptionCause() : null);
        this.numberOfFailedAttempts = numberOfFailedAttempts;
        this.lastFailedAttempt = lastFailedAttempt;
    }


    public int getNumberOfFailedAttempts() {
        return numberOfFailedAttempts;
    }


    public Attempt<?> getLastFailedAttempt() {
        return lastFailedAttempt;
    }
}
