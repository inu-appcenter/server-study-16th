package com.todolist.todolist.dto.todo;

import com.todolist.todolist.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private String contents;

    private Boolean isCompleted;

    private LocalDateTime dueAt;

}
