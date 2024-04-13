package com.jiyunio.todolist.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInDto {
    @NotBlank
    private String userId;

    @NotBlank
    private String userPw;
}
