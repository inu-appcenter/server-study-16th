package com.appcenter.practice.dto.reqeust;


import com.appcenter.practice.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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
