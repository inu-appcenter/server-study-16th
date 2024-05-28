package com.appcenter.practice.service;

import com.appcenter.practice.common.StatusCode;
import com.appcenter.practice.domain.Member;
import com.appcenter.practice.dto.request.member.SignupMemberReq;
import com.appcenter.practice.exception.CustomException;
import com.appcenter.practice.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks// mock 객체들의 의존성을 자동으로 주입해준다.
    private MemberService memberService;

    private SignupMemberReq signupMemberReq;


    @BeforeEach
    void beforeEach(){
        this.signupMemberReq=SignupMemberReq.builder()
                .email("example.naver.com")
                .password("example123!!")
                .nickname("냄B뚜껑")
                .build();

    }

    @Test
    @DisplayName("회원가입 성공")
    void signupTest1() {
        // Given(사전 조건 설정)
        Member member=signupMemberReq.toEntity(passwordEncoder);
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // When(테스트 실행)
        Member result=memberRepository.save(member);
        // Then(테스트 결과 검증)
        assertEquals(member,result);
    }

    @Test
    @DisplayName("회원가입 실패, 중복된 이메일")
    public void signupTest2(){
        given(memberRepository.existsByEmail(any())).willReturn(true);

        CustomException exception=assertThrows(CustomException.class, () ->{
            memberService.signup(signupMemberReq);
        });

        assertEquals(exception.getStatusCode().getMessage(), StatusCode.EMAIL_DUPLICATED.getMessage());
    }


}