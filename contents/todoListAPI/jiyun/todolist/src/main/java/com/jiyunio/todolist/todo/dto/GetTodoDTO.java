package com.jiyunio.todolist.todo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GetTodoDTO {
    private String content;

    private Boolean checked;

    private String category;

    private LocalDate writeDate;

    private LocalDate setDate;

    @Builder
    protected GetTodoDTO(String content, Boolean checked, String category, LocalDate writeDate, LocalDate setDate) {
        this.content = content;
        this.checked = checked;
        this.category = category;
        this.writeDate = writeDate;
        this.setDate = setDate;
    }
}
