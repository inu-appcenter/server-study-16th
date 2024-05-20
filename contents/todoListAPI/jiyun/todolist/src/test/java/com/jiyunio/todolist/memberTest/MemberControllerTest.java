package com.jiyunio.todolist.memberTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiyunio.todolist.member.Member;
import com.jiyunio.todolist.member.MemberRepository;
import com.jiyunio.todolist.member.MemberService;
import com.jiyunio.todolist.member.dto.ChangeUserPwDTO;
import com.jiyunio.todolist.member.dto.SignInDTO;
import com.jiyunio.todolist.member.dto.SignUpDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 Controller")
    void SignUpTest() throws Exception {
        //given
        SignUpDTO signUpDTO = SignUpDTO.builder()
                .userId("wldbs")
                .userPw("qwer1234!")
                .userEmail("wldbs@naver.com")
                .confirmUserPw("qwer1234!")
                .build();

        String body = mapper.writeValueAsString(signUpDTO);

        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/signUp")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("로그인 Controller")
    void SignInTest() throws Exception {
        //given
        SignInDTO signInDTO = SignInDTO.builder()
                .userId("wldbs")
                .userPw("qwer1234!")
                .build();

        Member member = Member.builder()
                .userId("wldbs")
                .userPw("qwer1234!")
                .userEmail("wldbs@google.com")
                .build();

        memberRepository.save(member);

        String body = mapper.writeValueAsString(signInDTO);

        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/signIn")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("회원 비밀번호 수정 Controller")
    void UpdateMemberTest() throws Exception {
        //given
        ChangeUserPwDTO changeUserPwDTO = ChangeUserPwDTO.builder()
                .userPw("minji123!")
                .changePw("qwer1234!")
                .confirmChangePw("qwer1234!")
                .build();

        String body = mapper.writeValueAsString(changeUserPwDTO);

        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/{id}/update", 1L)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("회원탈퇴 Test")
    void DeleteMemberTest() throws Exception {
        //given
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/{id}/delete", 17L)
                        .param("userPw", "qwer1234!"))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }
}
