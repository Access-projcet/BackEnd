package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.type.ErrorType;

public class VisitFormException extends GlobalException{

    public VisitFormException(ErrorType statusCode) {
        super(statusCode);
    }
}
