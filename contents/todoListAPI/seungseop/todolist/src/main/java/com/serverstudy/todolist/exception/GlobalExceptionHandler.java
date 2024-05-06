package com.serverstudy.todolist.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.serverstudy.todolist.exception.ErrorCode.DUPLICATE_RESOURCE;
import static com.serverstudy.todolist.exception.ErrorCode.INVALID_PARAMETER;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler /*extends ResponseEntityExceptionHandler*/ {    // ResponseEntityExceptionHandler 상속 시 Spring MVC Exception을 @Override 하여 커스텀한 ErrorResponse를 반환할 수 있음

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        log.error("커스텀 예외 발생, throw CustomException : {}", ex.getErrorCode());
        return ErrorResponse.from(ex.getErrorCode());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("데이터 무결성 위반 예외 발생, throw DataIntegrityViolationException : {}", ex.getMessage());
        return ErrorResponse.from(DUPLICATE_RESOURCE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        log.error("valid 유효성 검사 예외 발생, throw MethodArgumentNotValidException : {}", ex.getMessage());

        BindingResult bindingResult = ex.getBindingResult();

        List<String> messageList = new ArrayList<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String messageBuilder = fieldError.getDefaultMessage() +
                    " 입력된 값: [" +
                    fieldError.getRejectedValue() +
                    "]";
            messageList.add(messageBuilder);
        }

        return ErrorResponse.of(INVALID_PARAMETER, messageList);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex){
        log.error("validated 유효성 검사 예외 발생, throw MethodArgumentNotValidException : {}", ex.getMessage());

        Iterator<ConstraintViolation<?>> iterator = ex.getConstraintViolations().iterator();

        List<String> messageList = new ArrayList<>();

        while (iterator.hasNext()) {
            final ConstraintViolation<?> constraintViolation = iterator.next();
            String messageBuilder = constraintViolation.getMessage() +
                    " 입력된 값: [" +
                    constraintViolation.getInvalidValue() +
                    "]";
            messageList.add(messageBuilder);
        }

        return ErrorResponse.of(INVALID_PARAMETER, messageList);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("JSON 데이터 형식 예외 발생, throw HttpMessageNotReadableException : {}", ex.getMessage());

        if (ex.getCause() instanceof MismatchedInputException mismatchedInputException) {
            String message = mismatchedInputException.getPath().get(0).getFieldName() + " 필드의 타입이 올바르지 않습니다.";
            return ErrorResponse.of(INVALID_PARAMETER, message);
        }
        return ErrorResponse.of(INVALID_PARAMETER, "확인할 수 없는 형태의 데이터가 들어왔습니다");
    }

}