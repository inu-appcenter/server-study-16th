package com.todolist.todolist.dto.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.todolist.todolist.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoResponseDto {
    private Long id;
    private String title;
    private String contents;
    private boolean isCompleted;
    private LocalDateTime dueAt;
    private Long memberId;
}
