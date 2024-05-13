package com.appcenter.practice.exception;


import com.appcenter.practice.common.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException{

    private final StatusCode statusCode;
}
