package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.type.ErrorType;

public class CompanyException extends GlobalException{

    public CompanyException(ErrorType statusCode) {
        super(statusCode);
    }
}
