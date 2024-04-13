package com.jiyunio.todolist.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserPw {
    @NotBlank
    private String userPw;

    @NotBlank
    private String changePw;

    @NotBlank
    private String confirmChangePw;
}
