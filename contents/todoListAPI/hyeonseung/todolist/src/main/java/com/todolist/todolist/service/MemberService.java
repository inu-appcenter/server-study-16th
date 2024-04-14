package com.todolist.todolist.service;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.dto.member.MemberRequest;
import com.todolist.todolist.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*
    1. 가입
    2. 로그인
    3. 회원조회
    4. 수정
     */

    // 1. 회원가입
    public Member create(MemberRequest request) {
       Member member = Member.builder()
                .name(request.getName())
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .build();
       return memberRepository.save(member);

    }

    // 2. 로그인
    public boolean signIn(MemberRequest request){
       Member member = searchByLoginId(request.getLoginId());
        return member.getPassword().equals(request.getPassword());
    }

    // 3. loginId로 회원조회
    public Member searchByLoginId(String id){
        return memberRepository.findByLoginId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Member searchId(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // 4. 정보수정
    public Member update(Long id, MemberRequest request){
        Member member = searchId(id);
        if( request.getLoginId() != null){ //기존 정보와 동일한지 확인
            member.updateLoginId(request.getLoginId()); ;
        }
        if(request.getPassword() != null){
            member.updatePassword(request.getPassword());
        }
        if( request.getName() != null){
            member.updateName(request.getName());
        }
        return memberRepository.save(member);
    }



}
