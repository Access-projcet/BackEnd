package com.solver.solver_be.global.exception.exceptionType;

import com.solver.solver_be.global.type.ResponseCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GlobalException extends RuntimeException {

    private ResponseCode statusCode;
    public GlobalException(ResponseCode statusCode) {
        this.statusCode = statusCode;
    }
}