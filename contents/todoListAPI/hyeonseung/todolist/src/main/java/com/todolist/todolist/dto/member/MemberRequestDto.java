package com.todolist.todolist.dto.member;

import com.todolist.todolist.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    @Schema(example = "Alex")
    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Pattern(message = "잘못된 이름 형식입니다.",
            regexp = "^(?:[가-힣]{3,6}|[A-Za-z]{3,15})$")
    private String name;

    @Schema(example = "alex")
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(message = "잘못된 아이디 형식입니다.",
            regexp = "^[A-Za-z0-9]{3,12}" )
    private String loginId;

    @Schema(example = "Q1w2e3r4!")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 15자의 비밀번호여야 합니다.",
            regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}$")
    private String password;


    @Getter
    public static class LoginRequestDto{
        @NotBlank(message = "아이디를 입력해주세요.")
        private String loginId;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

}
