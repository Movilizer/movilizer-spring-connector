package com.movilizer.connector.exception;

public class MovilizerException extends RuntimeException {

    public MovilizerException(String message) {
        super(message);
    }

    public MovilizerException(Throwable throwable) {
        super(throwable);
    }

    public MovilizerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
