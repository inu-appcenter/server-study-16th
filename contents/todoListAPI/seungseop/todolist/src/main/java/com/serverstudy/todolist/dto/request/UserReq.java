package com.serverstudy.todolist.dto.request;

import com.serverstudy.todolist.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface UserReq {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class UserPost {

        @NotBlank(message = "이메일은 공백으로 입력할 수 없습니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$"
                , message = "이메일 형식이 올바르지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호는 공백으로 입력할 수 없습니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}"
                , message = "비밀번호는 영문 대소문자, 숫자가 포함된 8~16자리여야 합니다.")
        private String password;

        @NotBlank(message = "닉네임은 공백으로 입력할 수 없습니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$"
                , message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
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
    class UserPatch {

        @NotBlank(message = "비밀번호는 공백으로 입력할 수 없습니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}"
                , message = "비밀번호는 영문 대소문자, 숫자가 포함된 8~16자리여야 합니다.")
        private String password;

        @NotBlank(message = "닉네임은 공백으로 입력할 수 없습니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$"
                , message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        private String nickname;
    }


}
