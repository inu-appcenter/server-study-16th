package com.todolist.todolist.validators;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    private ResponseEntity<ErrorResponseDto> ErrorResponse(BaseException e){
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponseDto errorResponse = ErrorResponseDto.fromErrorCode(errorCode);
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }
}
