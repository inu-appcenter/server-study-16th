package com.serverstudy.todolist.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<Enum, java.lang.Enum> {

    @Override
    public boolean isValid(java.lang.Enum value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; //null 값 허용 여부, true: 허용
        }

        Class<?> reflectionEnumClass = value.getDeclaringClass();
        return Arrays.asList(reflectionEnumClass.getEnumConstants()).contains(value);
    }
}