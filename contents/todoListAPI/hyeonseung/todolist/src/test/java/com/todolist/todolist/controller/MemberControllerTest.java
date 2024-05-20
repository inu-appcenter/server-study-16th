package com.todolist.todolist.controller;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.dto.member.MemberRequestDto;
import com.todolist.todolist.dto.member.MemberResponseDto;
import com.todolist.todolist.service.MemberService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
  //  private Gson gson;

    void createMember() throws Exception {
        MemberRequestDto requestDto = new MemberRequestDto("test","test123", "test123!@");
        MemberResponseDto responseDto = new MemberResponseDto(1L, "test","test123", LocalDateTime.now(), LocalDateTime.now());

        when(memberService.create(any(MemberRequestDto.class))).thenReturn(responseDto);

//        mockMvc.perform(post("/api/member")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(String.valueOf(requestDto))
//                .andExpect(status().isCreated())
//                .andExpect()
    }
    private void setMemberId(Member member, Long id) {
        try {
            Field idField = Member.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(member, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
