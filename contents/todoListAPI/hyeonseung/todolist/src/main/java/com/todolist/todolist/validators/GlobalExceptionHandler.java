package com.todolist.todolist.validators;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    private ResponseEntity<ErrorResponseDto> ErrorResponse(BaseException e){
        ErrorCode errorCode = e.getErrorCode();
        return  ErrorResponseDto.fromErrorCode(errorCode);
    }

    // Bean Validation 예외
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ErrorResponseDto> errors = new ArrayList<>();

        for(FieldError fieldError: fieldErrors){
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                    .code(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
            errors.add(errorResponseDto);
        }
        return ResponseEntity.status(status).body(errors);


    }
}
