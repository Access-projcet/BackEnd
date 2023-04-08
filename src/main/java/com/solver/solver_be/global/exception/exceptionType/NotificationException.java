package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.type.ResponseCode;

public class NotificationException extends GlobalException {

    private ResponseCode statusCode;

    public NotificationException(ResponseCode statusCode) {
        super(statusCode);
    }
}
