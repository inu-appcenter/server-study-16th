package com.appcenter.practice.service;


import com.appcenter.practice.domain.Member;
import com.appcenter.practice.dto.reqeust.SignupMemberReq;
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
    public long signup(SignupMemberReq reqDto){
        if(memberRepository.existsByEmail(reqDto.getEmail()))
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        Member member=reqDto.toEntity();
        return memberRepository.save(member).getId();
    }
}
