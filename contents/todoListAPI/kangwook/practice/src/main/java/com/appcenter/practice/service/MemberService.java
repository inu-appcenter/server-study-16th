package com.appcenter.practice.service;


import com.appcenter.practice.domain.Member;
import com.appcenter.practice.dto.reqeust.member.LoginMemberReq;
import com.appcenter.practice.dto.reqeust.member.SignupMemberReq;
import com.appcenter.practice.exception.CustomException;
import com.appcenter.practice.common.StatusCode;
import com.appcenter.practice.repository.MemberRepository;
import com.appcenter.practice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long signup(SignupMemberReq reqDto){
        if(memberRepository.existsByEmail(reqDto.getEmail()))
            throw new CustomException(StatusCode.EMAIL_DUPLICATED);
        Member member=reqDto.toEntity();
        member.passwordEncode(passwordEncoder);
        member.addUserAuthority();
        return memberRepository.save(member).getId();
    }

    public String login(LoginMemberReq loginMemberReq){
        Member member= memberRepository.findByEmail(loginMemberReq.getEmail())
                .orElseThrow(()->new CustomException(StatusCode.EMAIL_INVALID));
        if(!passwordEncoder.matches(loginMemberReq.getPassword(),member.getPassword())){
            throw new CustomException(StatusCode.PASSWORD_INVALID);
        }

        return jwtTokenProvider.createAccessToken(member.getId(), member.getRole().name());
    }

}
