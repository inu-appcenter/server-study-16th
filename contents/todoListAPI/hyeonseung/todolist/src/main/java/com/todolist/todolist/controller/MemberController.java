package com.todolist.todolist.controller;


import com.todolist.todolist.dto.member.MemberResponseDto;
import com.todolist.todolist.dto.member.MemberRequestDto;
import com.todolist.todolist.service.MemberService;
import com.todolist.todolist.validators.ErrorMessageHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ErrorMessageHandler messageHandler;

    // 회원가입
    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody @Valid MemberRequestDto request, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
          Map<String,String> errorMessages = messageHandler.errorResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }

        MemberResponseDto responseDto = memberService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 회원 정보 수정
    @PutMapping("/{memberId}")
    public ResponseEntity<?> updateMember(@RequestBody @Valid MemberRequestDto request, BindingResult bindingResult, @PathVariable Long memberId){

        if(bindingResult.hasErrors()){
            Map<String,String> errorMessages = messageHandler.errorResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }

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
