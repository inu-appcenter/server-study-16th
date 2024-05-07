package com.appcenter.practice.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    /* 2xx: 성공 */
    // Member
    MEMBER_CREATE(CREATED,"회원 가입 완료"),
    // TodoList
    TODO_CREATE(CREATED,"할 일 생성 완료"),
    TODO_FOUND(OK,"할 일 조회 완료"),
    TODO_UPDATE(OK,"할 일 수정 완료"),
    TODO_DELETE(OK,"할 일 삭제 완료"),
    // Comment
    COMMENT_CREATE(CREATED,"댓글 생성 완료"),
    COMMENT_FOUND(OK,"댓글 조회 완료"),
    COMMENT_UPDATE(OK,"할 일 수정 완료"),
    COMMENT_DELETE(OK,"할 일 삭제 완료"),

    /* 400 BAD_REQUEST : 잘못된 요청 */
    LOGIN_ID_INVALID(BAD_REQUEST,"아이디가 틀렸습니다."),
    INPUT_VALUE_INVALID(BAD_REQUEST,"유효하지 않은 입력입니다."),

    /* 404 NOT_FOUNT : 존재하지 않는 리소스 */
    MEMBER_NOT_EXIST(NOT_FOUND,"존재하지 않는 멤버입니다."),
    TODO_NOT_EXIST(NOT_FOUND,"존재하지 않는 할 일입니다."),
    COMMENT_NOT_EXIST(NOT_FOUND,"존재하지 않는 댓글입니다."),

    /* 409 CONFLICT : 리소스 충돌 */
    EMAIL_DUPLICATED(CONFLICT,"이미 존재하는 이메일입니다.");


    private final HttpStatus status;
    private final String message;
}
