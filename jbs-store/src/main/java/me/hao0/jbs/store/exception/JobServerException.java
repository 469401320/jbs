package me.hao0.jbs.store.exception;


public class JobServerException extends RuntimeException {

    public JobServerException() {
        super();
    }

    public JobServerException(String message) {
        super(message);
    }

    public JobServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobServerException(Throwable cause) {
        super(cause);
    }

    protected JobServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
