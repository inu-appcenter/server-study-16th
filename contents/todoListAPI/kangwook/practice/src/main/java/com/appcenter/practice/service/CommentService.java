package com.appcenter.practice.service;


import com.appcenter.practice.domain.Comment;
import com.appcenter.practice.domain.Todo;
import com.appcenter.practice.dto.reqeust.comment.AddCommentReq;
import com.appcenter.practice.dto.reqeust.comment.UpdateCommentReq;
import com.appcenter.practice.dto.response.comment.ReadCommentRes;
import com.appcenter.practice.exception.CustomException;
import com.appcenter.practice.common.StatusCode;
import com.appcenter.practice.repository.CommentRepository;
import com.appcenter.practice.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;


    public List<ReadCommentRes> getCommentList(){
        return commentRepository.findAll().stream()
                .map(comment-> ReadCommentRes.from(comment))
                .collect(Collectors.toList());
    }

    public ReadCommentRes getComment(Long id){
        Comment comment=findByCommentId(id);
        return ReadCommentRes.from(comment);
    }

    @Transactional
    public Long saveComment(Long todoId, AddCommentReq reqDto){
        Todo todo= todoRepository.findById(todoId)
                .orElseThrow(()-> new CustomException(StatusCode.TODO_NOT_EXIST));
        return commentRepository.save(reqDto.toEntity(todo)).getId();
    }

    @Transactional
    public Long updateComment(Long id, UpdateCommentReq reqDto){
        Comment comment= findByCommentId(id);
        comment.changeContent(reqDto.getContent());
        return id;
    }

    @Transactional
    public Long deleteComment(Long id){
        Comment comment=findByCommentId(id);
        comment.changeDeleted(true);
        return id;
    }

    private Comment findByCommentId(Long id){
       return commentRepository.findById(id)
                .orElseThrow(()->new CustomException(StatusCode.COMMENT_NOT_EXIST));
    }
}
