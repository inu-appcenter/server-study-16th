package com.appcenter.practice.dto.request.todo;

import com.appcenter.practice.domain.Member;
import com.appcenter.practice.domain.Todo;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddTodoReq {

    @NotBlank(message = "content는 필수 입력 값입니다.")
    private String content;

    @Builder
    public AddTodoReq(String content) {
        this.content = content;
    }

    public Todo toEntity(Member member){
        return Todo.builder()
                .content(content)
                .completed(false)
                .member(member)
                .build();
    }
}
