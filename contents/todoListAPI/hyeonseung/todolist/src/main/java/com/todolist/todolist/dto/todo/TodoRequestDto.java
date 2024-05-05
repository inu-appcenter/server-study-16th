package com.todolist.todolist.dto.todo;

import com.todolist.todolist.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TodoRequestDto {
    private String title;
    private String contents;
    private boolean isCompleted;
    private LocalDateTime dueAt;
}
