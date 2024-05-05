package com.appcenter.practice.dto.reqeust.member;


import com.appcenter.practice.domain.Member;
import lombok.Getter;

@Getter
public class SignupMemberReq {
    private String email;
    private String password;
    private String nickname;


    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
