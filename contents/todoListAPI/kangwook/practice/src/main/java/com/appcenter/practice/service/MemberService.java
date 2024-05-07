package com.appcenter.practice.service;


import com.appcenter.practice.domain.Member;
import com.appcenter.practice.dto.reqeust.member.SignupMemberReq;
import com.appcenter.practice.exception.CustomException;
import com.appcenter.practice.common.StatusCode;
import com.appcenter.practice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long signup(SignupMemberReq reqDto){
        if(memberRepository.existsByEmail(reqDto.getEmail()))
            throw new CustomException(StatusCode.EMAIL_DUPLICATED);
        Member member=reqDto.toEntity();
        member.addUserAuthority();
        return memberRepository.save(member).getId();
    }
}
