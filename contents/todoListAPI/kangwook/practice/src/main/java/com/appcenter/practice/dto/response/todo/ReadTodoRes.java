package com.appcenter.practice.dto.response.todo;

import com.appcenter.practice.domain.Todo;
import lombok.Getter;

@Getter
public class ReadTodoRes {
    private final Long id;
    private final String content;
    private final Boolean completed;


    private ReadTodoRes(Long id, String content, Boolean completed) {
        this.id = id;
        this.content = content;
        this.completed = completed;
    }

    public static ReadTodoRes from(Todo todo){
        return new ReadTodoRes(todo.getId(),todo.getContent(),todo.getCompleted());
    }

}
