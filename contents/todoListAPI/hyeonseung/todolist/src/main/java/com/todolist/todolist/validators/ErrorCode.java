package com.todolist.todolist.validators;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 400

    // 409 CONFLICT ERROR
    INVALID_ID(CONFLICT,"이미 존재하는 아이디입니다."),

    // 404 NOT_FOUND ERROR
    NOT_EXIST_ID(NOT_FOUND,"존재하지 않는 회원입니다."),
    NOT_EXIST_TODO(NOT_FOUND, "존재하지 않는 Todo입니다."),
    NOT_MATCH_TODO_ID(NOT_FOUND,"회원과 일치하는 TODOLIST가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;

}
