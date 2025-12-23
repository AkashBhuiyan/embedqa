package com.akash.embedqa.domain.exception;

/**
 * Author: akash
 * Date: 23/12/25
 */
public class ApiExecutionException extends DomainException {
    public ApiExecutionException(String message) {
        super(message);
    }

    public ApiExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
