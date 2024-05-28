package com.appcenter.practice.dto.request.comment;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCommentReq {

    @NotBlank(message = "content는 필수 입력 값입니다.")
    String content;

    @Builder
    public UpdateCommentReq(String content) {
        this.content = content;
    }
}
