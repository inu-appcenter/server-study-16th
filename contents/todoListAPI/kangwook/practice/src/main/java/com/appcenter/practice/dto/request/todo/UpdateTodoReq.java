package com.appcenter.practice.dto.request.todo;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateTodoReq {

    @NotBlank(message = "content는 필수 입력값입니다.")
    String content;

    @Builder
    public UpdateTodoReq(String content) {
        this.content = content;
    }
}
