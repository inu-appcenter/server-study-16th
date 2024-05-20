package com.jiyunio.todolist.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInDTO {
    @NotBlank(message = "아이디를 입력하세요.")
    @Schema(example = "qwe123")
    private String userId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Schema(example = "qwer1234!")
    private String userPw;
}
