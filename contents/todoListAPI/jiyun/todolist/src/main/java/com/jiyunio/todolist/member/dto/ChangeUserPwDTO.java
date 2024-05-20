package com.jiyunio.todolist.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
<<<<<<< HEAD
import lombok.*;
=======
import lombok.Getter;
import lombok.Setter;
>>>>>>> 22a000ba52704fbf78c6e07ecff4aed83785374c

@Getter
@Setter
@Schema(description = "회원 비밀번호 수정")
<<<<<<< HEAD
@Builder
=======
>>>>>>> 22a000ba52704fbf78c6e07ecff4aed83785374c
public class ChangeUserPwDTO {
    @NotBlank(message = "비밀번호를 입력하세요.")
    @Schema(description = "회원 비밀번호")
    private String userPw;

    @NotBlank(message = "변경 비밀번호를 입력하세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")
    @Schema(description = "회원 수정 비밀번호", example = "qwer1234!")
    private String changePw;

    @NotBlank(message = "확인 비밀번호를 입력하세요.")
    @Schema(description = "회원 수정 확인 비밀번호")
    private String confirmChangePw;
}
