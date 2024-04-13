package com.jiyunio.todolist.todo.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TodoDto {
    @Lob // 길이 제한 X
    @NotBlank
    private String content;

    @NotBlank
    private Boolean checked;

    @NotBlank
    private String category;

    @NotBlank
    private LocalDate writeDate;

    @NotBlank
    private LocalDate setDate;

    @Builder
    protected TodoDto(String content, Boolean checked, String category, LocalDate writeDate, LocalDate setDate) {
        this.content = content;
        this.checked = checked;
        this.category = category;
        this.writeDate = writeDate;
        this.setDate = setDate;
    }
}
