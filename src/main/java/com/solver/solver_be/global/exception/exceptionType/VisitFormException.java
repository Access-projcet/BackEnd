package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.type.ResponseCode;

public class VisitFormException extends GlobalException{

    public VisitFormException(ResponseCode statusCode) {
        super(statusCode);
    }
}
