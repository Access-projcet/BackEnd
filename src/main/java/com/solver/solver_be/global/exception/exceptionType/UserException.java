package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.type.ResponseCode;

public class UserException extends GlobalException {
    public UserException(ResponseCode statusCode) {
        super(statusCode);
    }
}