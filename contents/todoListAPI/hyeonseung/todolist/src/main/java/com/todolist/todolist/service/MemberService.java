package com.todolist.todolist.service;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.dto.member.MemberLoginResponseDto;
import com.todolist.todolist.dto.member.MemberMapper;
import com.todolist.todolist.dto.member.MemberRequestDto;
import com.todolist.todolist.dto.member.MemberResponseDto;
import com.todolist.todolist.repository.MemberRepository;

import com.todolist.todolist.validators.BaseException;
import com.todolist.todolist.validators.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
            throw new BaseException(ErrorCode.DUPLICATE_LOGINID);

       Member member = MemberMapper.INSTANCE.toEntity(request);
       memberRepository.save(member);

       return MemberMapper.INSTANCE.toDto(member);

    }

    // 2. 로그인
    public MemberLoginResponseDto login(MemberRequestDto.LoginRequestDto request){
       Member member = throwFindbyLoginId(request.getLoginId());
       if (!member.getPassword().equals(request.getPassword()))
           throw new BaseException(ErrorCode.UNAUTHORIZED_LOGIN);
       return new MemberLoginResponseDto(member.getId());
    }

    // 3. 회원 검색
    public MemberResponseDto searchId(Long id){
        Member member = throwFindbyId(id);
        return MemberMapper.INSTANCE.toDto(member);
    }

    // 4. 회원 전체 검색
    public List<MemberResponseDto> searchAll(){
        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

    }

    // 5. 정보수정
    public MemberResponseDto update(Long id, MemberRequestDto request){
        Member member = throwFindbyId(id);

        if(memberRepository.existsByLoginId(request.getLoginId()))
            throw new BaseException(ErrorCode.DUPLICATE_LOGINID);
        else {
            member.updateLoginId(request.getLoginId());
            member.updatePassword(request.getPassword());
            member.updateName(request.getName());
        }

       memberRepository.save(member);

       return MemberMapper.INSTANCE.toDto(member);
    }

    // 6. 회원 삭제
    public void delete(Long id){
        Member member = throwFindbyId(id);
        memberRepository.deleteById(id);

    }

    // 회원 조회 메서드
    private Member throwFindbyId(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() ->  new BaseException(ErrorCode.NOT_EXIST_ID));
    }

    // 회원 조회 - 로그인 아이디 조회 메서드
    private Member throwFindbyLoginId(String loginId){
        return  memberRepository.findByLoginId(loginId)
                .orElseThrow(()-> new BaseException(ErrorCode.NOT_EXIST_ID));
    }
}
