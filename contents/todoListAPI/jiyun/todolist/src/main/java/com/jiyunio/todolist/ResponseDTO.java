package com.jiyunio.todolist;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO {
    Long id;
    String userId;
    String msg;

    @Builder
    ResponseDTO(Long id, String userId, String msg) {
        this.id = id;
        this.userId = userId;
        this.msg = msg;
    }
}
