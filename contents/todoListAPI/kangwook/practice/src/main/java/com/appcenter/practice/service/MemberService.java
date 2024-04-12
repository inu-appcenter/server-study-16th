package com.appcenter.practice.service;


import com.appcenter.practice.domain.Member;
import com.appcenter.practice.dto.reqeust.SignupMemberReq;
import com.appcenter.practice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long signup(SignupMemberReq reqDto){
        Member member=reqDto.toEntity();
        memberRepository.save(member);
        return member.getId();
    }
}
