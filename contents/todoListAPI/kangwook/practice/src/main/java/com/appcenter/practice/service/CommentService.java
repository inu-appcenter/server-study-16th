package com.appcenter.practice.service;


import com.appcenter.practice.domain.Todo;
import com.appcenter.practice.dto.reqeust.AddCommentReq;
import com.appcenter.practice.dto.reqeust.UpdateCommentReq;
import com.appcenter.practice.dto.response.ReadCommentRes;
import com.appcenter.practice.repository.CommentRepository;
import com.appcenter.practice.repository.TodoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.appcenter.practice.domain.QComment.comment;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public List<ReadCommentRes> getCommentList(){
        List<ReadCommentRes> commentList=queryFactory
                .select(Projections.constructor(ReadCommentRes.class,comment.id,comment.content,comment.deleted))
                .from(comment)
                .fetch();
        return commentList;
    }
    public void saveComment(Long todoId, AddCommentReq reqDto){
        Todo todo= todoRepository.findById(todoId).get();
        commentRepository.save(reqDto.toEntity(todo));
    }

    public Long updateComment(UpdateCommentReq reqDto){
        commentRepository.findById(reqDto.getId()).get().changeContent(reqDto.getContent());
        return reqDto.getId();
    }

    public Long deletedTrue(Long id){
        commentRepository.findById(id).get().changeDeleted(true);
        return id;
    }
}
