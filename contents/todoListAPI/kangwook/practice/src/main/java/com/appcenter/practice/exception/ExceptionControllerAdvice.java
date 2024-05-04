package com.appcenter.practice.exception;


import com.appcenter.practice.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@RestControllerAdvice
public class ExceptionControllerAdvice{

    /*
        MethodArgumentNotValidException는 유효성 검사에서 실패하면 나타나는 예외로 bindingReult에 에러를 담는다.
        bindingResult는 유효성검사에 실패해 예외가 발생하면 담아주는 객체이다.
        bindingResult가 없으면 400오류가 발생해 컨트롤러를 호출하지않고 오류페이지로 이동한다.
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e){

        ErrorResponse errorResponse= ErrorResponse.builder()
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus().toString())
                .message(ErrorCode.INVALID_INPUT_VALUE.getMessage())
                .errors(ErrorResponse.ValidationError.from(e.getBindingResult()))
                .build();
        return ResponseEntity.status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(errorResponse);
    }


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
        ErrorResponse errorResponse=ErrorResponse.builder()
                .status(e.getErrorCode().getStatus().toString())
                .message(e.getErrorCode().getMessage())
                .errors(new ArrayList<>())
                .build();
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }
}
