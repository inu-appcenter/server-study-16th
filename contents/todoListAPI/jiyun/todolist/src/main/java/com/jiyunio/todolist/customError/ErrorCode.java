package com.jiyunio.todolist.customError;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 400 Bad Request
    NOT_SAME_CONFIRM_PASSWORD("400_Bad_Request", "비밀번호 확인이 맞지 않습니다."),
    WRONG_USERID_PASSWORD("400_Bad_Request", "아이디 및 비밀번호가 맞지 않습니다."),

    // 404 Not Found
    NOT_EXIST_MEMBER("404_Not_Found", "회원이 존재하지 않습니다."),
    NOT_EXIST_TODO("404_Not_Found", "TODO가 존재하지 않습니다."),

    // 409 Conflict 중복된 값
    EXIST_USERID("409_Conflict", "이미 존재하는 아이디입니다."),
    EXIST_EMAIL("409_Conflict", "이미 존재하는 이메일입니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
