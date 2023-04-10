package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.type.ErrorType;

public class AccessException extends GlobalException{

    public AccessException(ErrorType statusCode) {
        super(statusCode);
    }
}
