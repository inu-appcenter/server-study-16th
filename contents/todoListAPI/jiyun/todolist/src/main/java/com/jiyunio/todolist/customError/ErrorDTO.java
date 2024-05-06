package com.jiyunio.todolist.customError;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@Builder
public class ErrorDTO {
    String code;
    String msg;

    public static ResponseEntity<ErrorDTO> toResponseEntity(CustomException e) {
        ErrorCode error = e.getErrorCode();
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorDTO.builder()
                        .code(error.getCode())
                        .msg(error.getMessage())
                        .build());
    }
}
