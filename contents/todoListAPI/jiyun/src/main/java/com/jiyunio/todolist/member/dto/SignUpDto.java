package com.jiyunio.todolist.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDto {
    @NotBlank
    private String userId;

    @NotBlank
    private String userPw;

    @NotBlank
    private String userEmail;

    @NotBlank
    private String confirmUserPw;
}
