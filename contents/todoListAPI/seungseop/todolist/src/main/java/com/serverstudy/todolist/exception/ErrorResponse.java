package com.serverstudy.todolist.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
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
