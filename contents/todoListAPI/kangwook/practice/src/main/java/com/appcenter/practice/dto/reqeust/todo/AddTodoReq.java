package com.appcenter.practice.dto.reqeust.todo;

import com.appcenter.practice.domain.Member;
import com.appcenter.practice.domain.Todo;
import lombok.Getter;

@Getter
public class AddTodoReq {
    private String email;
    private String content;

    public Todo toEntity(Member member){
        return Todo.builder()
                .content(content)
                .completed(false)
                .member(member)
                .build();
    }
}
