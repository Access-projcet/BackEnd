package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.response.ResponseCode;

public class AccessException extends GlobalException{

    public AccessException(ResponseCode statusCode) {
        super(statusCode);
    }
}
