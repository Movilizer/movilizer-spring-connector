package com.movilizer.mds.connector.exceptions;

/**
 * @author Pavel Kotlov
 *         <p>
 *         This is thrown if some paring error occured. While mapping DataContainer or Masterdata.
 */
public class MovilizerMappingException extends RuntimeException {

    public MovilizerMappingException(String message) {
        super(message);
    }

    public MovilizerMappingException(Throwable throwable) {
        super(throwable);
    }

    public MovilizerMappingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
