package com.serverstudy.todolist.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRes {

    private final long id;

    private final String email;

    private final String nickname;

    @Builder
    private UserRes(long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}