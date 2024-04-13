package com.jiyunio.todolist.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateTodoDto {
    @NotBlank
    private String content;

    @NotBlank
    private String category;

    @NotBlank
    private LocalDate writeDate;

    @NotBlank
    private LocalDate setDate;
}
