package com.todolist.todolist.controller;


import com.todolist.todolist.config.JwtTokenFilter;
import com.todolist.todolist.dto.member.MemberLoginResponseDto;
import com.todolist.todolist.dto.member.MemberResponseDto;
import com.todolist.todolist.dto.member.MemberRequestDto;
import com.todolist.todolist.security.CustomUserDetailService;
import com.todolist.todolist.security.JwtTokenProvider;
import com.todolist.todolist.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Value("${jwt.secret")
    private String key;

    private final JwtTokenProvider jwtTokenProvider;

    private CustomUserDetailService userDetailService;

    @Operation(summary = "회원가입")
    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestBody MemberRequestDto request) {

        MemberResponseDto responseDto = memberService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<String> loginMember(@RequestBody @Valid MemberRequestDto.LoginRequestDto request){
        MemberLoginResponseDto responseDto = memberService.login(request);

        String jwtToken = jwtTokenProvider.createToken(request.getLoginId());

        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
       // return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @Operation(summary = "회원정보 수정")
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> updateMember(@RequestBody @Valid MemberRequestDto request, @PathVariable Long memberId){

        MemberResponseDto responseDto = memberService.update(memberId, request);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "Id별 회원 조회")
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> readOneMember(@PathVariable Long memberId){
        MemberResponseDto responseDto = memberService.searchId(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "일괄 회원 조회")
    @GetMapping
    public  ResponseEntity<List<MemberResponseDto>> readAllMember(){
        List<MemberResponseDto> responseDto = memberService.searchAll();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "회원 삭제")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> delete(@PathVariable Long memberId){
        memberService.delete(memberId);
        return ResponseEntity.noContent().build();
    }

}
