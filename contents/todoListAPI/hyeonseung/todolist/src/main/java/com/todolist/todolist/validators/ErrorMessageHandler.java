package com.todolist.todolist.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component  //@Autoweired 자동주입
public class ErrorMessageHandler {

    public Map<String,String> errorResult(BindingResult bindingResult) {
        Map<String,String> errorMessages = new HashMap<>();

        for(FieldError error : bindingResult.getFieldErrors()){
            errorMessages.put(error.getField(), error.getDefaultMessage());
        }

        return errorMessages;
    }
}
