package com.appcenter.practice.dto.reqeust;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateTodoReq {
    Long id;
    String content;

}
