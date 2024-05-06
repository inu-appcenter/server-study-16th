package com.jiyunio.todolist.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserPwDTO {
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String userPw;

    @NotBlank(message = "변경 비밀번호를 입력하세요.")
    private String changePw;

    @NotBlank(message = "확인 비밀번호를 입력하세요.")
    private String confirmChangePw;
}
