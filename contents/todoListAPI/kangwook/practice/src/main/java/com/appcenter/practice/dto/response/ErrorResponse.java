package com.appcenter.practice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)// Null 값인 필드 json으로 보낼 시 제외
public class ErrorResponse {
    private final String message;
    private final List<ValidationError> validationErrors;

    @Builder
    private ErrorResponse(String message, List<ValidationError> validationErrors) {
        this.message = message;
        this.validationErrors = validationErrors;
    }

    @Getter
    public static class ValidationError{
        private String field;
        private String value;
        private String reason;

        private ValidationError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

//        1.@Valid 는 MethodArgumentNotValidException 을 발생시킨다.
//        2.MethodArgumentNotValidException 에는 여러 값들이 있으며, 그 중 BindingResult 라는 객체에 예외에 대한 좀 더 자세하고 많은 정보들이 담겨있다.
//        BindingResult 객체 안에서 error 와 관련된 필요한 값을 가져오기 위해 BindingResult.getFieldErrors() 메소드를 이용하여 BindingResult 가 갖고 있는 errors 를 FieldError 라는 객체 형태로 반환받고
//        3.FieldError 객체에서 필요한 값들을 사용자(개발자) 가 정의한 예외 객체에 매핑하여 사용한다.
//        Field : 객체에서 예외가 발생한 field
//        RejectedValue : 어떤 값으로 인해 예외가 발생하였는지
//        DefaultMessage : 해당 예외가 발생했을 때 제공할 message 는 무엇인지
        public static List<ValidationError> from(BindingResult bindingResult){
            List<FieldError> fieldErrors= bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new ValidationError(
                            error.getField(),
                            (error.getRejectedValue()==null) ? null : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
