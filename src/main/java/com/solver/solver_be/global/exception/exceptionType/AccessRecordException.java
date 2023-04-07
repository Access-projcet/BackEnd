package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.type.ResponseCode;

public class AccessRecordException extends GlobalException{

    public AccessRecordException(ResponseCode statusCode) {
        super(statusCode);
    }
}
