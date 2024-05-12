package com.todolist.todolist.controller;


import com.todolist.todolist.dto.member.MemberResponseDto;
import com.todolist.todolist.dto.member.MemberRequestDto;
import com.todolist.todolist.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestBody MemberRequestDto request) {

        MemberResponseDto responseDto = memberService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 회원 정보 수정
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> updateMember(@RequestBody @Valid MemberRequestDto request, @PathVariable Long memberId){

        MemberResponseDto responseDto = memberService.update(memberId, request);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 회원 검색 - 단일조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> readOneMember(@PathVariable Long memberId){
        MemberResponseDto responseDto = memberService.searchId(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 회원 검색 - 전체
    @GetMapping
    public  ResponseEntity<List<MemberResponseDto>> readAllMember(){
        List<MemberResponseDto> responseDto = memberService.searchAll();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 회원 삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> delete(@PathVariable Long memberId){
        memberService.delete(memberId);
        return ResponseEntity.noContent().build();
    }


}
