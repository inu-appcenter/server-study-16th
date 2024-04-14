package com.todolist.todolist.service;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.dto.member.MemberMapper;
import com.todolist.todolist.dto.member.MemberRequestDto;
import com.todolist.todolist.dto.member.MemberResponse;
import com.todolist.todolist.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public MemberResponse create(MemberRequestDto request) {
       Member member = MemberMapper.INSTANCE.toEntity(request);
       memberRepository.save(member);

       return MemberMapper.INSTANCE.toDto(member);

    }

    // 2. 로그인
    public boolean signIn(MemberRequestDto request){
       Member member = memberRepository.findByLoginId(request.getLoginId())
               .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
       return member.getPassword().equals(request.getPassword());
    }


    public MemberResponse searchId(Long id){
        Member member =  memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return MemberMapper.INSTANCE.toDto(member);
    }

    // 4. 정보수정
    public MemberResponse update(Long id, MemberRequestDto request){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        member.updateLoginId(request.getLoginId()); ;
        member.updatePassword(request.getPassword());
        member.updateName(request.getName());

       memberRepository.save(member);

       return MemberMapper.INSTANCE.toDto(member);
    }



}
