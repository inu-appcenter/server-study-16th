package com.appcenter.practice.controller;

import com.appcenter.practice.dto.reqeust.SignupMemberReq;
import com.appcenter.practice.service.MemberService;
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
    public ResponseEntity<?> signup(@RequestBody SignupMemberReq reqDto){
        return ResponseEntity
                .status(201)
                .body(memberService.signup(reqDto));
    }

}
