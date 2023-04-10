package com.solver.solver_be.global.response;

import com.solver.solver_be.global.type.ErrorType;
import com.solver.solver_be.global.type.SuccessType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalResponseDto {
    private int statusCode;
    private String message;
    private Object data;

    public static GlobalResponseDto of(ErrorType errorType, Object data) {
        return GlobalResponseDto.builder()
                .statusCode(errorType.getStatusCode())
                .message(errorType.getMessage())
                .data(data)
                .build();
    }

    public static GlobalResponseDto of(SuccessType successType, Object data) {
        return GlobalResponseDto.builder()
                .statusCode(successType.getStatusCode())
                .message(successType.getMessage())
                .data(data)
                .build();
    }


    public static GlobalResponseDto of(ErrorType errorType) {
        return GlobalResponseDto.builder()
                .statusCode(errorType.getStatusCode())
                .message(errorType.getMessage())
                .build();
    }

    public static GlobalResponseDto of(SuccessType successType) {
        return GlobalResponseDto.builder()
                .statusCode(successType.getStatusCode())
                .message(successType.getMessage())
                .build();
    }
}