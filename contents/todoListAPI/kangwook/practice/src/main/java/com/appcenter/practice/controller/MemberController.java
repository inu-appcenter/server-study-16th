package com.appcenter.practice.controller;

import com.appcenter.practice.dto.reqeust.member.LoginMemberReq;
import com.appcenter.practice.dto.reqeust.member.SignupMemberReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.appcenter.practice.common.StatusCode.MEMBER_CREATE;
import static com.appcenter.practice.common.StatusCode.MEMBER_LOGIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;


    @PostMapping
    public ResponseEntity<CommonResponse<Long>> signup( @RequestBody @Valid SignupMemberReq reqDto){
        return ResponseEntity
                .status(MEMBER_CREATE.getStatus())
                .body(CommonResponse.of(MEMBER_CREATE.getMessage(), memberService.signup(reqDto)));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<CommonResponse<Void>> login(@RequestBody @Valid LoginMemberReq signInMemberReqDto){

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION,  memberService.login(signInMemberReqDto))
                .body(CommonResponse.of(MEMBER_LOGIN.getMessage(), null));
    }


}
