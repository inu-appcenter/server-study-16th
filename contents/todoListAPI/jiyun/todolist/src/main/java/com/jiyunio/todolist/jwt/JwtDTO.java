package com.jiyunio.todolist.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtDTO {
    private String grantType;
    private String userId;
    private String accessToken;

    @Builder
    protected JwtDTO(String grantType, String userId, String accessToken){
        this.grantType = grantType;
        this.userId = userId;
        this.accessToken = accessToken;
    }
}
