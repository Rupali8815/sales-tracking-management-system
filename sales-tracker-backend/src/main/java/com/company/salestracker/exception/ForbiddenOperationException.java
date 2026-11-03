package com.company.salestracker.exception;

/** Business-rule violation: the user may be logged in and have the permission, but not for THIS record. */
public class ForbiddenOperationException extends RuntimeException {
    public ForbiddenOperationException(String message) {
        super(message);
    }
}
