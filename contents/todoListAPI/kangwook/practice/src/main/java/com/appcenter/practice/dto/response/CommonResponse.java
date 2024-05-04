package com.appcenter.practice.dto.response;

import lombok.Getter;

@Getter
//@RequiredArgsConstructor(staticName = "of")
public class CommonResponse<T>{
    private final String status;
    private final String message;
    private final T response;

    private CommonResponse(String status, String message, T response) {
        this.status = status;
        this.message = message;
        this.response = response;
    }

    //<제네릭 타입> 반환타입 메소드이름()
    //클래스에 있는 T와 메소드의 T는 다른 타입이다.
    //이를 통해 메소드 호출 시 원하는 타입의 객체를 생성할 수 있다.
    public static <T> CommonResponse<T> of(String statusCode,String message, T response){
        return new CommonResponse<T>(statusCode,message,response);

    }
}
