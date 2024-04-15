package com.appcenter.practice.dto.response;

import com.appcenter.practice.domain.Todo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReadTodoRes {
    private Long id;
    private String content;
    private Boolean completed;

    @Builder
    public ReadTodoRes(Long id, String content, Boolean completed) {
        this.id = id;
        this.content = content;
        this.completed = completed;
    }

    public static ReadTodoRes from(Todo todo){
        return ReadTodoRes.builder()
                .id(todo.getId())
                .content(todo.getContent())
                .completed(todo.getCompleted())
                .build();
    }

}
