package com.appcenter.practice.service;

import com.appcenter.practice.common.StatusCode;
import com.appcenter.practice.domain.Member;
import com.appcenter.practice.domain.Todo;
import com.appcenter.practice.dto.reqeust.todo.AddTodoReq;
import com.appcenter.practice.dto.reqeust.todo.UpdateTodoReq;
import com.appcenter.practice.dto.response.todo.ReadTodoRes;
import com.appcenter.practice.exception.CustomException;
import com.appcenter.practice.repository.MemberRepository;
import com.appcenter.practice.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TodoService todoService;

    private AddTodoReq addTodoReq;

    private UpdateTodoReq updateTodoReq;

    private Todo todo;

    private Member member;

    @BeforeEach
    void beforeEach(){
        this.addTodoReq=AddTodoReq.builder()
                .email("example.naver.com")
                .content("토익하기")
                .build();

        this.updateTodoReq=UpdateTodoReq.builder()
                .content("오픽하기")
                .build();

        this.todo=addTodoReq.toEntity(member);
    }

    @Test
    @DisplayName("투두 생성 성공")
    void addTodoTest1(){
        // Given(사전 조건 설정)
        given(todoRepository.save(any(Todo.class))).willReturn(todo);

        // When(테스트 실행)
        Todo result=todoRepository.save(todo);
        // Then(테스트 결과 검증)
        assertEquals(todo,result);
    }

    @Test
    @DisplayName("투두 생성 실패, 멤버 없음")
    void addTodoTest2(){
        given(memberRepository.findByEmail(any())).willThrow(new CustomException(StatusCode.MEMBER_NOT_EXIST));

        CustomException exception=assertThrows(CustomException.class, () ->{
            todoService.saveTodo(addTodoReq);
        });

        assertEquals(StatusCode.MEMBER_NOT_EXIST.getMessage(),exception.getStatusCode().getMessage());
    }

    @Test
    @DisplayName("투두 조회 성공")
    void readTodoTest1(){
        given(todoRepository.findById(any())).willReturn(Optional.ofNullable(todo));

        ReadTodoRes result=todoService.getTodo(31L);

        assertEquals(todo.getContent(),result.getContent());

    }

    @Test
    @DisplayName("투두 조회 실패, 투두 없음")
    void readTodoTest2(){
        given(todoRepository.findById(any())).willThrow(new CustomException(StatusCode.TODO_NOT_EXIST));

        CustomException exception=assertThrows(CustomException.class,() ->{
            todoService.getTodo(31L);
        });

        assertEquals(StatusCode.TODO_NOT_EXIST.getMessage(),exception.getStatusCode().getMessage());

    }

    @Test
    @DisplayName("투두 수정 성공")
    void updateTodoTest1(){

        todo.changeContent(updateTodoReq.getContent());

        assertEquals(updateTodoReq.getContent(),todo.getContent());
    }

    @Test
    @DisplayName("투두 수정 실패, 투두 없음")
    void updateTodoTest2(){
        given(todoRepository.findById(any())).willThrow(new CustomException(StatusCode.TODO_NOT_EXIST));

        CustomException exception=assertThrows(CustomException.class,() ->{
            todoService.updateTodo(31L,updateTodoReq);
        });

        assertEquals(StatusCode.TODO_NOT_EXIST.getMessage(),exception.getStatusCode().getMessage());
    }
}