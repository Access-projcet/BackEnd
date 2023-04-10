package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.type.ErrorType;

public class AccessRecordException extends GlobalException{

    public AccessRecordException(ErrorType statusCode) {
        super(statusCode);
    }
}
