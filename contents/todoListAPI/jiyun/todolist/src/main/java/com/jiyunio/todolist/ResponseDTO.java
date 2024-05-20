package com.jiyunio.todolist;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO {
    String result;
    String msg;

    @Builder
    ResponseDTO(String result, String msg) {
        this.result = result;
        this.msg = msg;
    }
}
