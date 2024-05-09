package com.jiyunio.todolist.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateTodoDTO {
    @NotBlank(message = "todo를 작성해주세요.")
    private String content;

    @NotNull
    private Boolean checked;

    @NotBlank
    private String category;

    @NotNull(message = "작성 일자를 선택해주세요.")
    private LocalDate writeDate;

    @NotNull(message = "설정 일자를 선택해주세요.")
    private LocalDate setDate;
}
