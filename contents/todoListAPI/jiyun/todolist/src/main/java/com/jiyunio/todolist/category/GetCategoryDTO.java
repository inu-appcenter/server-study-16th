package com.jiyunio.todolist.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "카테고리 조회")
public class GetCategoryDTO {
    private String category;

    @Builder
    protected GetCategoryDTO(String category) {
        this.category = category;
    }
}
