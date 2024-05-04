package com.appcenter.practice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_LOGIN_ID(HttpStatus.BAD_REQUEST,"아이디가 틀렸습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호가 틀렸습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST,"유효하지 않은 입력입니다."),

    /* 404 NOT_FOUNT : 존재하지 않는 리소스 */
    NOT_EXIST_MEMBER(HttpStatus.NOT_FOUND,"존재하지 않는 멤버입니다."),
    NOT_EXIST_TODO(HttpStatus.NOT_FOUND,"존재하지 않는 투두입니다."),
    NOT_EXIST_COMMENT(HttpStatus.NOT_FOUND,"존재하지 않는 댓글입니다."),

    /* 409 CONFLICT : 리소스 충돌 */
    DUPLICATED_EMAIL(HttpStatus.CONFLICT,"이미 존재하는 이메일입니다.");


    private final HttpStatus status;
    private final String message;
}
