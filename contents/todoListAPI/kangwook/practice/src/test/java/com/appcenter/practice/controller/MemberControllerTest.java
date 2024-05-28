package com.appcenter.practice.controller;

import com.appcenter.practice.common.StatusCode;
import com.appcenter.practice.dto.request.member.SignupMemberReq;
import com.appcenter.practice.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc; //서버에 배포하지 않고도 mvc기능을 테스트 할 수 있다.

    @Autowired
    private Gson gson; // 객체를 json으로 변환해주는 구글의 라이브러리


    @Test
    @DisplayName("회원가입 성공")
    void signupTest1() throws Exception{ // Exception 던지지 않으면 mockMvc에서 exception을 핸들할 수 없다.
        SignupMemberReq signupMemberReq=SignupMemberReq.builder()
                .email("example@naver.com")
                .password("example123!!")
                .nickname("냄B뚜껑")
                .build();

        String content=gson.toJson(signupMemberReq);
        given(memberService.signup(any())).willReturn(1L);

        this.mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(StatusCode.MEMBER_CREATE.getMessage()))
                .andDo(print());

    }

    @Test
    @DisplayName("회원가입 실패, 유효성 검사 실패")
    void signupTest2() throws Exception{
        SignupMemberReq signupMemberReq=SignupMemberReq.builder()
                .email("example")
                .password("example")
                .nickname("1")
                .build();

        String content=gson.toJson(signupMemberReq);
        given(memberService.signup(any())).willReturn(1L);

        this.mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(StatusCode.INPUT_VALUE_INVALID.getMessage()))
                .andDo(print());
    }
}