package com.appcenter.practice.dto.reqeust.comment;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateCommentReq {

    @NotBlank(message = "content는 필수 입력 값입니다.")
    String content;
}
