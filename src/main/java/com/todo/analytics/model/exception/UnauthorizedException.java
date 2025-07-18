package com.todo.analytics.model.exception;

public class UnauthorizedException extends WebAppException {

    public UnauthorizedException() {
        super("You are not authorized to perform this action", 401);
    }

    public UnauthorizedException(String message) {
        super(message, 401);
    }
}