package com.jiyunio.todolist.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {
    @NotBlank(message = "아이디를 입력하세요.")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=\\S+$).{5,10}", message = "아이디 : 5~10자")
    private String userId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호: 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용하십쇼.")
    private String userPw;

    @NotBlank(message = "이메일를 입력하세요.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String userEmail;

    @NotBlank(message = "확인 비밀번호를 입력하세요.")
    private String confirmUserPw;
}
