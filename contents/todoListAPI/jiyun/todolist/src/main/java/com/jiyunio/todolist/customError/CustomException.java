package com.jiyunio.todolist.customError;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ErrorCode errorCode;

    public CustomException(HttpStatus httpStatus, ErrorCode errorCode){
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
