package com.jiyunio.todolist.todo.dto;

import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateTodoDto {

    @Lob // 길이 제한 X
    private String content;
    private Boolean checked;
    private String category;
    private LocalDate writeDate;
    private LocalDate setDate;
}
