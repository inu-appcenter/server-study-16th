package com.appcenter.practice.service;


import com.appcenter.practice.domain.Comment;
import com.appcenter.practice.domain.Todo;
import com.appcenter.practice.dto.reqeust.AddCommentReq;
import com.appcenter.practice.dto.reqeust.UpdateCommentReq;
import com.appcenter.practice.dto.response.ReadCommentRes;
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
                .map(comment->ReadCommentRes.from(comment))
                .collect(Collectors.toList());
    }

    @Transactional
    public long saveComment(Long todoId, AddCommentReq reqDto){
        Todo todo= todoRepository.findById(todoId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 할일 입니다."));
        return commentRepository.save(reqDto.toEntity(todo)).getId();
    }

    @Transactional
    public long updateComment(Long id, UpdateCommentReq reqDto){
        Comment comment= commentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        comment.changeContent(reqDto.getContent());
        return id;
    }

    @Transactional
    public long deleteComment(Long id){
        Comment comment=commentRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 댓글입니다."));
        comment.changeDeleted(true);
        return id;
    }
}
