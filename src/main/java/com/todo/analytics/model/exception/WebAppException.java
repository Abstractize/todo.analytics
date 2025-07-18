package com.todo.analytics.model.exception;

public abstract class WebAppException extends RuntimeException {

    private final int statusCode;

    protected WebAppException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}