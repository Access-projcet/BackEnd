package com.solver.solver_be.global.exception;

import com.solver.solver_be.global.exception.exceptionType.*;
import com.solver.solver_be.global.response.GlobalResponseDto;
import com.solver.solver_be.global.type.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // UserException Handler
    @ExceptionHandler(UserException.class)
    public ResponseEntity<GlobalResponseDto> handleUserException(UserException e) {
        ErrorType errorType = e.getStatusCode();
        log.error(errorType.getMessage());
        return ResponseEntity.badRequest()
                .body(GlobalResponseDto.of(errorType));
    }

    // CompanyException
    @ExceptionHandler(CompanyException.class)
    public ResponseEntity<GlobalResponseDto> handleCompanyException(CompanyException e) {
        ErrorType errorType = e.getStatusCode();
        log.error(errorType.getMessage());
        return ResponseEntity.badRequest()
                .body(GlobalResponseDto.of(errorType));
    }

    // VisitFormException
    @ExceptionHandler(VisitFormException.class)
    public ResponseEntity<GlobalResponseDto> handleVisitFormException(VisitFormException e) {
        ErrorType errorType = e.getStatusCode();
        log.error(errorType.getMessage());
        return ResponseEntity.badRequest()
                .body(GlobalResponseDto.of(errorType));
    }

    // AccessRecordException
    @ExceptionHandler(AccessRecordException.class)
    public ResponseEntity<GlobalResponseDto> handleAccessRecordException(AccessRecordException e) {
        ErrorType errorType = e.getStatusCode();
        log.error(errorType.getMessage());
        return ResponseEntity.badRequest()
                .body(GlobalResponseDto.of(errorType));
    }

    // AccessException
    @ExceptionHandler(AccessException.class)
    public ResponseEntity<GlobalResponseDto> handleAccessException(AccessException e) {
        ErrorType errorType = e.getStatusCode();
        log.error(errorType.getMessage());
        return ResponseEntity.badRequest()
                .body(GlobalResponseDto.of(errorType));
    }

    // GlobalException Handler
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<GlobalResponseDto> handleGlobalException(GlobalException e) {
        ErrorType errorType = e.getStatusCode();
        log.error(errorType.getMessage());
        return ResponseEntity.badRequest()
                .body(GlobalResponseDto.of(errorType));
    }

    // NotificationException Handler
    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<GlobalResponseDto> handleNotificationException(NotificationException e) {
        ErrorType errorType = e.getStatusCode();
        log.error(errorType.getMessage());
        return ResponseEntity.badRequest()
                .body(GlobalResponseDto.of(errorType));
    }

    // Validation Handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponseDto> handleMethodException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error(message);
        return ResponseEntity.badRequest()
                .body(new GlobalResponseDto(HttpStatus.BAD_REQUEST.value(), message, null));
    }

}