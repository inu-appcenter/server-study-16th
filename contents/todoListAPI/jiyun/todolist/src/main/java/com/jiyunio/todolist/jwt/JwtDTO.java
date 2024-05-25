package com.jiyunio.todolist.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtDTO {
    private String userId;
    private String accessToken;

    @Builder
    protected JwtDTO(String userId, String accessToken){
        this.userId = userId;
        this.accessToken = accessToken;
    }
}
