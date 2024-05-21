package com.todolist.todolist.dto.todo;

import com.todolist.todolist.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TodoRequestDto {

    @Schema(example= "할일 추가")
    @Size(min=1, max=30)
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @Schema(example = "add Todolist")
    private String contents;

    @Schema(example = "false")
    private Boolean isCompleted;

    private LocalDateTime dueAt;

}
