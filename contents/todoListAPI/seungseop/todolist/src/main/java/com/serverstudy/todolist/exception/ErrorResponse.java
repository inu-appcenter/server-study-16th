package com.serverstudy.todolist.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "에러 응답 DTO")
@Getter
public class ErrorResponse {
    @Schema(title = "에러 발생 시간", description = "에러 발생 시간", example = "2024-05-15T05:15:43.823Z")
    private final LocalDateTime timestamp = LocalDateTime.now();
    @Schema(title = "HTTP 상태 코드", description = "Http 상태 코드 (4XX)", example = "400")
    private final int status;
    @Schema(title = "HTTP 상태 메시지", description = "HTTP 상태 메시지", example = "BAD_REQUEST")
    private final String error;
    @Schema(title = "커스텀 에러 코드", description = "커스텀 에러 코드", example = "INVALID_PARAMETER")
    private final String code;
    @Schema(title = "커스텀 에러 메시지", description = "커스텀 에러 메시지", example = "[userId] 널이어서는 안됩니다 입력된 값: [null]")
    private final List<String> message = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private ErrorResponse(int status, String error, String code, String message, List<String> messageList) {
        this.status = status;
        this.error = error;
        this.code = code;

        if (message != null) this.message.add(message);
        if (messageList != null) this.message.addAll(messageList);
    }

    public static ResponseEntity<ErrorResponse> from(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .build()
                );
    }

    public static ResponseEntity<ErrorResponse> of(ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .message(message)
                        .build()
                );
    }

    public static ResponseEntity<ErrorResponse> of(ErrorCode errorCode, List<String> messageList) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .messageList(messageList)
                        .build()
                );
    }
}
