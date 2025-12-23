package com.akash.embedqa.domain.exception;

/**
 * Author: akash
 * Date: 23/12/25
 */
public abstract class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}