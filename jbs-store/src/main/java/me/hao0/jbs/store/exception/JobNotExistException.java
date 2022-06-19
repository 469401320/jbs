package me.hao0.jbs.store.exception;


public class JobNotExistException extends RuntimeException {

    private Long id;

    public JobNotExistException(Long id) {
        super();
        this.id = id;
    }

    public JobNotExistException(String message) {
        super(message);
    }

    public JobNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobNotExistException(Throwable cause) {
        super(cause);
    }

    protected JobNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Long getId() {
        return id;
    }
}
