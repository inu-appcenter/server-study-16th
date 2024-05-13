package com.appcenter.practice.dto.reqeust.todo;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateTodoReq {

    @NotBlank(message = "content는 필수 입력값입니다.")
    String content;

}
