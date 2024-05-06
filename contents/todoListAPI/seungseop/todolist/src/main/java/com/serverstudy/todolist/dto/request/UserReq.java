package com.serverstudy.todolist.dto.request;

import com.serverstudy.todolist.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface UserReq {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class UserPost {

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
    class UserPut {

        private String password;

        private String nickname;
    }


}
