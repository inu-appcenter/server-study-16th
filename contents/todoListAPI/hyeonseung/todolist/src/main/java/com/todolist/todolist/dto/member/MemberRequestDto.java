package com.todolist.todolist.dto.member;

import com.todolist.todolist.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Pattern(message = "잘못된 이름 형식입니다.",
            regexp = "^(?:[가-힣]{3,6}|[A-Za-z]{3,12})$")
    private String name;

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(message = "잘못된 아이디 형식입니다.",
            regexp = "^[a-z0-9]{3,12}" )
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 15자의 비밀번호여야 합니다.",
            regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}")
    private String password;



}
