package com.appcenter.practice.dto.reqeust.comment;


import com.appcenter.practice.domain.Comment;
import com.appcenter.practice.domain.Todo;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddCommentReq {

    @NotBlank(message = "content는 필수 입력값입니다.")
    private String content;

    @Builder
    public AddCommentReq(String content) {
        this.content = content;
    }

    public Comment toEntity(Todo todo){
        return Comment.builder()
                .content(content)
                .deleted(false)
                .toDo(todo)
                .build();
    }
}
