package com.serverstudy.todolist.dto;

import com.serverstudy.todolist.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface UserDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class PostReq {

        @NotNull
        @Email(message = "이메일 형식이 아닙니다.")
        private String email;

        @NotNull
        private String password;

        @NotNull
        private String nickname;

        public User toEntity() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class PutReq {

        private String password;

        private String nickname;
    }

    @Getter
    class Response {

        private final long id;

        private final String email;

        private final String nickname;

        @Builder
        private Response(long id, String email, String nickname) {
            this.id = id;
            this.email = email;
            this.nickname = nickname;
        }
    }
}
