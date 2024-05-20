package com.appcenter.practice.dto.reqeust.todo;

import com.appcenter.practice.domain.Member;
import com.appcenter.practice.domain.Todo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddTodoReq {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "content는 필수 입력 값입니다.")
    private String content;

    @Builder
    public AddTodoReq(String email, String content) {
        this.email = email;
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
