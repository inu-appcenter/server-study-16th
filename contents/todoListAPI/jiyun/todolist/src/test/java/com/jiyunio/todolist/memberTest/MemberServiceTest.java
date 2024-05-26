package com.jiyunio.todolist.memberTest;

import com.jiyunio.todolist.member.Member;
import com.jiyunio.todolist.member.MemberRepository;
import com.jiyunio.todolist.member.MemberService;
import com.jiyunio.todolist.member.dto.SignInDTO;
import com.jiyunio.todolist.member.dto.SignUpDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원 회원가입 test")
    void SignUpTest() {
        //given
        SignUpDTO signUpDTO = SignUpDTO.builder()
                .userId("wldbswldbs")
                .userPw("qwe123!")
                .confirmUserPw("qwe123!")
                .userEmail("wldbs@google.com")
                .build();

        //when
        String userId = memberService.signUp(signUpDTO);

        //then
        assertThat(userId).isEqualTo(signUpDTO.getUserId());
    }

    @DisplayName("로그인 test")
    @Test
    void SignInTest() {
        //given
        Member member = Member.builder()
                .userId("wldbs")
                .userPw("qwe123!")
                .userEmail("jiyun@naver.com")
                .build();

        memberRepository.save(member);

        SignInDTO signInDTO = SignInDTO.builder()
                .userId("wldbs")
                .userPw("qwe123!")
                .build();

        //when
        //String userId = memberService.signIn(signInDTO);

        //then
        //assertThat(userId).isEqualTo(signInDTO.getUserId());
    }

    @Test
    @DisplayName("회원탈퇴 Test")
    void DeleteTest() {
        //given
        Long id = 2L;
        String pw = "qwer1234!";

        //when
        memberService.deleteMember(id, pw);

        //then
        Assertions.assertEquals(3, memberRepository.count());
    }
}