package com.serverstudy.todolist.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_PARAMETER(BAD_REQUEST, "파라미터 값이 유효하지 않습니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    TODO_NOT_FOUND(NOT_FOUND, "해당 투두 정보를 찾을 수 없습니다"),
    FOLDER_NOT_FOUND(NOT_FOUND, "해당 폴더 정보를 찾을 수 없습니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "해당 데이터가 이미 존재합니다"),
    DUPLICATE_USER_EMAIL(CONFLICT, "해당 이메일이 이미 존재합니다"),
    DUPLICATE_FOLDER_NAME(CONFLICT, "해당 폴더명이 이미 존재합니다."),

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
