package com.todolist.todolist.validators;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    private final ErrorCode errorCode;


    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
