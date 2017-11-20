package com.movilizer.mds.connector.exceptions;

public class MovilizerConnectorException extends RuntimeException {

    public MovilizerConnectorException(String message) {
        super(message);
    }

    public MovilizerConnectorException(Throwable throwable) {
        super(throwable);
    }

    public MovilizerConnectorException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
