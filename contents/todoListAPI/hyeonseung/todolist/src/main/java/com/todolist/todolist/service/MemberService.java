package com.todolist.todolist.service;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.dto.member.MemberMapper;
import com.todolist.todolist.dto.member.MemberRequestDto;
import com.todolist.todolist.dto.member.MemberResponseDto;
import com.todolist.todolist.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

    // 3. 회원 검색
    public MemberResponseDto searchId(Long id){
        Member member =  memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return MemberMapper.INSTANCE.toDto(member);
    }

    // 4. 회원 전체 검색
    public List<MemberResponseDto> searchAll(){
        List<Member> members = memberRepository.findAll();
        List<MemberResponseDto>  memberList = new ArrayList<>();

        return members.stream()
                .map(MemberMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

//        for (Member member : members){
//            MemberResponseDto responseDto = MemberMapper.INSTANCE.toDto(member);
//            memberList.add(responseDto);
//        }
//        return memberList;
    }

    // 5. 정보수정
    public MemberResponseDto update(Long id, MemberRequestDto request){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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

}
