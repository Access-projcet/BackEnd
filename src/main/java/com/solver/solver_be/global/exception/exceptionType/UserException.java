package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.type.ErrorType;

public class UserException extends GlobalException {

    public UserException(ErrorType statusCode) {
        super(statusCode);
    }
}