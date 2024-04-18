package com.appcenter.practice.dto.reqeust.comment;


import com.appcenter.practice.domain.Comment;
import com.appcenter.practice.domain.Todo;
import lombok.Getter;

@Getter
public class AddCommentReq {
    private String content;

    public Comment toEntity(Todo todo){
        return Comment.builder()
                .content(content)
                .deleted(false)
                .toDo(todo)
                .build();
    }
}
