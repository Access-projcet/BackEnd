package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.type.ResponseCode;

public class CompanyException extends GlobalException{

    public CompanyException(ResponseCode statusCode) {
        super(statusCode);
    }
}
