package com.jiyunio.todolist.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
<<<<<<< HEAD
@Builder
=======
>>>>>>> 22a000ba52704fbf78c6e07ecff4aed83785374c
@Schema(description = "todo 생성 : todo checked 기본 값 False")
public class CreateTodoDTO {
    @NotBlank(message = "todo를 작성해주세요.")
    private String content;

    @NotBlank
    private String category;

    @NotNull(message = "작성 일자를 선택해주세요.")
    private LocalDate writeDate;

    @NotNull(message = "설정 일자를 선택해주세요.")
    private LocalDate setDate;
}
