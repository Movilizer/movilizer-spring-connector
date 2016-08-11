package com.movilizer.connector.exceptions;

/**
 * @author Pavel Kotlov
 *         <p>
 *         This is thrown if some paring error occured. While mapping DataContainer or Masterdata.
 */
public class MovilizerParsingException extends RuntimeException {

    public MovilizerParsingException(String message) {
        super(message);
    }

    public MovilizerParsingException(Throwable throwable) {
        super(throwable);
    }

    public MovilizerParsingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
