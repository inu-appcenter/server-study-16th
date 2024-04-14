package com.todolist.todolist.dto;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TodoRequest {
    private String title;
    private String contents;
    private boolean isCompleted;
    private LocalDateTime dueAt;
    private Member member;
    public Todo createTodo() {
        return Todo.builder()
                .title(title)
                .contents(contents)
                .isCompleted(isCompleted)
                .dueAt(dueAt)
                .build();
    }
}
