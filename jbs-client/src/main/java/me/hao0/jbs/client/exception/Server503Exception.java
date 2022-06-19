package me.hao0.jbs.client.exception;


public class Server503Exception extends RuntimeException {

    public Server503Exception() {
    }

    public Server503Exception(String message) {
        super(message);
    }

    public Server503Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public Server503Exception(Throwable cause) {
        super(cause);
    }

    public Server503Exception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
