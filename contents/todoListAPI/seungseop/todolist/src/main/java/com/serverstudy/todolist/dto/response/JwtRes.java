package com.serverstudy.todolist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "토큰 응답 DTO")
@Getter
public class JwtRes {
    @Schema(title = "Access Token", description = "Access Token", example = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJleGFtcGxlQGdtYWlsLmNvbSIsImlhdCI6MTcxNjgyNTQyOSwiZXhwIjoxNzE2ODI3MjI5fQ.qcvwPKY2LhvwR6OmQoTIsLGsjAFwLHdroe6aQ1q313xgL_A5X58bVMGc15_F0WVG")
    private String accessToken;

    @Builder
    private JwtRes(String accessToken) {
        this.accessToken = accessToken;
    }
}
