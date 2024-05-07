package com.todolist.todolist.service;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.dto.member.MemberMapper;
import com.todolist.todolist.dto.member.MemberRequestDto;
import com.todolist.todolist.dto.member.MemberResponseDto;
import com.todolist.todolist.repository.MemberRepository;

import com.todolist.todolist.validators.BaseException;
import com.todolist.todolist.validators.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*
    1. 가입
    2. 로그인
    3. 회원조회
    4. 회원전체조회
    4. 수정
    5. 삭제
    */

    // 1. 회원가입
    public MemberResponseDto create(MemberRequestDto request) {
        if (memberRepository.existsByLoginId(request.getLoginId()))
            throw new BaseException(ErrorCode.INVALID_ID);

       Member member = MemberMapper.INSTANCE.toEntity(request);
       memberRepository.save(member);

       return MemberMapper.INSTANCE.toDto(member);

    }

    // 2. 로그인
    public boolean signIn(MemberRequestDto request){
       Member member = findbyLoginId(request.getLoginId());
       return member.getPassword().equals(request.getPassword());
    }

    // 3. 회원 검색
    public MemberResponseDto searchId(Long id){
        Member member = findbyId(id);
        return MemberMapper.INSTANCE.toDto(member);
    }

    // 4. 회원 전체 검색
    public List<MemberResponseDto> searchAll(){
        List<Member> members = memberRepository.findAll();
        List<MemberResponseDto>  memberList = new ArrayList<>();

        return members.stream()
                .map(MemberMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

    }

    // 5. 정보수정
    public MemberResponseDto update(Long id, MemberRequestDto request){
        Member member = findbyId(id);
        member.updateLoginId(request.getLoginId()); ;
        member.updatePassword(request.getPassword());
        member.updateName(request.getName());
       memberRepository.save(member);

       return MemberMapper.INSTANCE.toDto(member);
    }

    // 6. 회원 삭제
    public void delete(Long id){
        memberRepository.deleteById(id);

    }


    // 회원 조회 메서드
    private Member findbyId(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() ->  new BaseException(ErrorCode.NOT_EXIST_ID));
    }

    // 회원 조회 - 로그인 아이디 조회 메서드
    private Member findbyLoginId(String loginId){
        return  memberRepository.findByLoginId(loginId)
                .orElseThrow(()-> new BaseException(ErrorCode.NOT_EXIST_ID));
    }
}
