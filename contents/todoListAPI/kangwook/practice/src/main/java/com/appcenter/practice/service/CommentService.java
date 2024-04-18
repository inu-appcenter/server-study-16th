package com.appcenter.practice.service;


import com.appcenter.practice.domain.Comment;
import com.appcenter.practice.domain.Todo;
import com.appcenter.practice.dto.reqeust.comment.AddCommentReq;
import com.appcenter.practice.dto.reqeust.comment.UpdateCommentReq;
import com.appcenter.practice.dto.response.comment.ReadCommentRes;
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
        Comment comment=commentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 comment입니다."));
        return ReadCommentRes.from(comment);
    }

    @Transactional
    public Long saveComment(Long todoId, AddCommentReq reqDto){
        Todo todo= todoRepository.findById(todoId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 todo입니다."));
        return commentRepository.save(reqDto.toEntity(todo)).getId();
    }

    @Transactional
    public Long updateComment(Long id, UpdateCommentReq reqDto){
        Comment comment= commentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 comment입니다."));
        comment.changeContent(reqDto.getContent());
        return id;
    }

    @Transactional
    public Long deleteComment(Long id){
        Comment comment=commentRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 comment입니다."));
        comment.changeDeleted(true);
        return id;
    }
}
