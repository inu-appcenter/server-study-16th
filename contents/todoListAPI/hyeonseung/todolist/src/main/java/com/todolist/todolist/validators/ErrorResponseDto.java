package com.todolist.todolist.validators;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponseDto {

    private final String code;
    private final String message;


    public static ResponseEntity<ErrorResponseDto> fromErrorCode(final ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponseDto.builder()
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .build()
                );
    }

}
