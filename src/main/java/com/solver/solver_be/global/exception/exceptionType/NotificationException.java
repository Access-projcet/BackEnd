package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.type.ErrorType;

public class NotificationException extends GlobalException {

    public NotificationException(ErrorType statusCode) {
        super(statusCode);
    }
}
