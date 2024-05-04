package com.appcenter.practice.controller;

import com.appcenter.practice.dto.reqeust.member.SignupMemberReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;


    @PostMapping
    public ResponseEntity<CommonResponse<Long>> signup( @RequestBody @Valid SignupMemberReq reqDto){
        return ResponseEntity
                .status(201)
                .body(CommonResponse.of("Created","member 생성 성공",memberService.signup(reqDto)));
    }

}
