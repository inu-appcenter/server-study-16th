package com.todolist.todolist.validators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponseDto {

    private String message;
    private String code;

    private ErrorResponseDto(ErrorCode errorCode){
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }
    public static ErrorResponseDto fromErrorCode(ErrorCode errorCode) {
        return new ErrorResponseDto(errorCode);
    }

}
