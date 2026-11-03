package com.company.salestracker.exception;

/** The record is in a state that does not allow this operation (e.g. deal already closed). */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
