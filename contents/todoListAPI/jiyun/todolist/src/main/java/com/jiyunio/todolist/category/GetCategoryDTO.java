package com.jiyunio.todolist.category;

import com.jiyunio.todolist.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCategoryDTO {
    private String category;

    @Builder
    protected GetCategoryDTO(String category) {
        this.category = category;
    }
}
