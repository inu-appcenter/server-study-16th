package com.jiyunio.todolist.customError;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorDTO> handleCustomException(CustomException e) {
        return ErrorDTO.toResponseEntity(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<List<ErrorDTO>> beanValidationException(MethodArgumentNotValidException e) {
        List<FieldError> list = e.getBindingResult().getFieldErrors();
        List<ErrorDTO> responseDTOList = new ArrayList<>();

        for (FieldError error : list) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .code("400_Bad_Request")
                    .msg(error.getDefaultMessage())
                    .build();

            responseDTOList.add(errorDTO);
        }
        return new ResponseEntity<>(responseDTOList, HttpStatus.BAD_REQUEST);
    }
}
