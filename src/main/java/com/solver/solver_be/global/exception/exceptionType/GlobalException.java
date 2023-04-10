package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.type.ErrorType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GlobalException extends RuntimeException {

    private ErrorType statusCode;
    public GlobalException(ErrorType statusCode) {
        this.statusCode = statusCode;
    }
}