package com.appcenter.practice.service;

import com.appcenter.practice.common.StatusCode;
import com.appcenter.practice.domain.Comment;
import com.appcenter.practice.domain.Todo;
import com.appcenter.practice.dto.reqeust.comment.AddCommentReq;
import com.appcenter.practice.dto.reqeust.todo.UpdateTodoReq;
import com.appcenter.practice.dto.response.comment.ReadCommentRes;
import com.appcenter.practice.exception.CustomException;
import com.appcenter.practice.repository.CommentRepository;
import com.appcenter.practice.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;

    @Mock
    TodoRepository  todoRepository;

    @InjectMocks
    CommentService commentService;

    private AddCommentReq addCommentReq;
    private UpdateTodoReq updateTodoReq;
    private Comment comment;
    private Todo todo;

    @BeforeEach
    void beforeEach(){
        this.addCommentReq=AddCommentReq.builder()
                .content("굿잡")
                .build();

        this.updateTodoReq=UpdateTodoReq.builder()
                .content("낫뱃")
                .build();

        this.comment=addCommentReq.toEntity(todo);
    }


    @Test
    @DisplayName("댓글 생성 성공")
    void addCommentTest1(){
        given(commentRepository.save(any())).willReturn(comment);

        Comment result=commentRepository.save(comment);

        assertEquals(result,comment);
    }

    @Test
    @DisplayName("댓글 생성 실패, 투두 없음")
    void addCommentTest2(){
        given(todoRepository.findById(any())).willThrow(new CustomException(StatusCode.TODO_NOT_EXIST));

        CustomException exception=assertThrows(CustomException.class,() ->{
            commentService.saveComment(31L,addCommentReq);
        });

        assertEquals(StatusCode.TODO_NOT_EXIST.getMessage(),exception.getStatusCode().getMessage());
    }

    @Test
    @DisplayName("단일 댓글 조회 성공")
    void readCommentTest1(){
        given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));

        ReadCommentRes result=commentService.getComment(31L);

        assertEquals(comment.getContent(),result.getContent());
    }

    @Test
    @DisplayName("단일 댓글 조회 실패, 댓글 없음")
    void readCommentTest2(){
        given(commentRepository.findById(any())).willThrow(new CustomException(StatusCode.COMMENT_NOT_EXIST));

        CustomException exception=assertThrows(CustomException.class,() ->{
            commentService.getComment(31L);
        });

        assertEquals(StatusCode.COMMENT_NOT_EXIST.getMessage(),exception.getStatusCode().getMessage());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateCommentTest1(){

       comment.changeContent(updateTodoReq.getContent());

       assertEquals(updateTodoReq.getContent(),comment.getContent());
    }

    @Test
    @DisplayName("댓글 수정 실패, 댓글 없음")
    void updateCommentTest2(){
        given(commentRepository.findById(any())).willThrow(new CustomException(StatusCode.COMMENT_NOT_EXIST));

        CustomException exception=assertThrows(CustomException.class,() ->{
            commentService.getComment(31L);
        });

        assertEquals(StatusCode.COMMENT_NOT_EXIST.getMessage(),exception.getStatusCode().getMessage());

    }



}