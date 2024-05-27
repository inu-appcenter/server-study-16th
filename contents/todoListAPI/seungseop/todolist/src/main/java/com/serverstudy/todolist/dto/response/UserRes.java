package com.serverstudy.todolist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "유저 응답 DTO")
@Getter
public class UserRes {

    @Schema(title = "유저 Id", description = "유저 Id", example = "1")
    private final long id;

    @Schema(title = "이메일", description = "이메일", example = "example@gmail.com")
    private final String email;

    @Schema(title = "닉네임", description = "닉네임", example = "ex닉네임1")
    private final String nickname;

    @Builder
    private UserRes(long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}