package com.jiyunio.todolist.customError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 400 Bad Request
    EXIST_USERID("001_EXIST_USERID", "이미 존재하는 아이디입니다."),
    EXIST_EMAIL("002_EXIST_EMAIL", "이미 존재하는 이메일입니다."),
    NOT_SAME_CONFIRM_PASSWORD("003_NOT_SAME_CONFIRM_PASSWORD", "비밀번호 확인이 맞지 않습니다."),

    //404 Not Found
    NOT_EXIST_MEMBER("401_NOT_EXIST_MEMBER", "회원이 존재하지 않습니다."),
    NOT_EXIST_TODO("402_NOT_EXIST_MEMBER", "TODO가 존재하지 않습니다."),
    NOT_EXIST_USERID("403_NOT_EXIST_USERID", "아이디가 존재하지 않습니다."),
    WRONG_PASSWORD("404_WRONG_PASSWORD", "비밀번호가 다릅니다.");

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
