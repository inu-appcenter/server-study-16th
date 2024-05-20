package com.todolist.todolist.service;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.dto.member.MemberLoginResponseDto;
import com.todolist.todolist.dto.member.MemberMapper;
import com.todolist.todolist.dto.member.MemberRequestDto;
import com.todolist.todolist.dto.member.MemberResponseDto;
import com.todolist.todolist.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    private Member member;
    private MemberRequestDto requestDto;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;


    @BeforeEach
    void setUp() {
        member = Member.builder()
                .name("test")
                .loginId("test123")
                .password("test123!@")
                .build();

        memberService = new MemberService(memberRepository);
        requestDto = new MemberRequestDto("test", "test123", "test123!@");

    }

    @Test
    @DisplayName("멤버 생성")
    void createMemberSuccess(){

        member = MemberMapper.INSTANCE.toEntity(requestDto);

        when(memberRepository.existsByLoginId(requestDto.getLoginId())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberResponseDto responseDto = memberService.create(requestDto);

        assertThat(responseDto.getLoginId()).isEqualTo(requestDto.getLoginId());
        assertThat(responseDto.getName()).isEqualTo(requestDto.getName());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("로그인")
    void LoginMember() {
        when(memberRepository.findByLoginId(requestDto.getLoginId())).thenReturn(Optional.of(member));

        MemberRequestDto.LoginRequestDto loginRequestDto = new MemberRequestDto.LoginRequestDto("test123" ,"test123!@");
        MemberLoginResponseDto responseDto = memberService.login(loginRequestDto);

        assertThat(responseDto.getLoginId()).isEqualTo(requestDto.getLoginId());
    }


    @Test
    @DisplayName("단일 회원 검색")
    void SearchMemberById(){
        setMemberId(member, 1L);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        MemberResponseDto responseDto =  memberService.searchId(1L);

        assertThat(responseDto.getId()).isEqualTo(member.getId());
        assertThat(responseDto.getLoginId()).isEqualTo(requestDto.getLoginId());
        assertThat(responseDto.getName()).isEqualTo(requestDto.getName());
    }

    @Test
    @DisplayName("전체 회원 검색")
    void SearchAll(){
        when(memberRepository.findAll()).thenReturn(Arrays.asList(member));

        List<MemberResponseDto> responseDtos = memberService.searchAll();

        assertThat(responseDtos).hasSize(1);
        assertThat(responseDtos.get(0).getLoginId()).isEqualTo(requestDto.getLoginId());
    }

    @Test
    @DisplayName("회원정보 수정")
    void updateMember() {
        setMemberId(member,1L);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(memberRepository.existsByLoginId(any(String.class))).thenReturn(false);

        MemberRequestDto newRequestDto = new MemberRequestDto("newTest", "newTest123", "test456!@#");

        MemberResponseDto responseDto = memberService.update(1L, newRequestDto);

        //assertThat(responseDto.getId()).isEqualTo(member.getId());
        assertThat(responseDto.getLoginId()).isEqualTo(newRequestDto.getLoginId());
        assertThat(responseDto.getName()).isEqualTo(newRequestDto.getName());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원삭제")
    void deleteMember(){
        setMemberId(member, 1L);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        memberService.delete(1L);

        verify(memberRepository, times(1)).deleteById(1L);

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
