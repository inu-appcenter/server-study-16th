# Spring에서의 예외처리

### Spring의 기본적인 예외 처리 방식

아래와 같이 예외를 던지는 메서드가 있을 때 이를 호출하는 URL `/errorExample`에 요청을 보내는 경우,

```java
@RestController
public class SimpleController {

    @GetMapping(path = "/errorExample")
    public String invokeError() {
        throw new IllegalArgumentException();
    }
}
```

웹으로 요청을 보낸 경우라면 다음과 같이 스프링 부트가 기본 제공하는 에러 페이지가 표시된다.

![img1 daumcdn](https://github.com/inu-appcenter/server-study-16th/assets/86196038/740d435e-07d2-4b91-bcf4-cd70b809c5c7)

스프링에는 에러 처리를 위한 BasicErrorController가 구현되어 있는데,

BasicErrorController는 요청의 Accept 헤더 값이 text/html일 때 예외가 발생하면, `/error`라는 경로로 재요청을 보내준다.

해당 페이지는 `/error` 경로에 등록된 기본 에러 페이지이다.

만약 스스로 작성한 커스텀 페이지를 보여주고 싶다면, 뷰 템플릿 경로에 커스텀 페이지 파일을 만들어서 넣어두면 된다.

ex) 4xx.html (400대 오류 페이지)
      5xx.html (500대 오류 페이지)

만약 페이지 변경 뿐만 아니라 더 상세한 예외의 내용을 응답에 담고 싶다면, BasicErrorController를 상속한 `@Controller` 클래스를 만들어 `errorHtml()` 메서드와 `error()` 메서드를 재정의 해주면 된다.

다만 이 방법은 url만 알면 누구나 마음대로 error 페이지에 접근할 수 있다는 문제가 있다.

또한 직접 지정한 HTTP Status Code와 함께 원하는 형태로 보내주고 싶을 수 있는데 이를 해결하기 위해 `@ExceptionHandler`를 사용할 수 있다.

### @ExceptionHandler

`@ExceptionHandler`는 Controller 계층에서 발생하는 예외를 잡아서 메서드로 처리해주는 기능이다.

Service와 Repository 계층에서 발생하는 예외는 제외한다.

간단한 예시로

```java
@Controller
public class SimpleController {

    // ...

    @ExceptionHandler(value = {FileSystemException.class, RemoteException.class})
    public ResponseEntity<String> handle(IOException ex) {
        // ...
    }
}
```

위와 같이 Controller 클래스 안에서 `@ExceptionHandler`을 붙인 메서드를 통해 해당 컨트롤러 안에서 발생할 수 있는 예외를 처리할 수 있다. 

`@ExceptionHandler` 어노테이션의 `value` 값으로는 어떤 Exception을 처리할 것인지 넘겨줄 수 있다.

`value`를 설정하지 않으면 모든 Exception을 잡게 된다.

다만, Handler를 통해 들어오는 예외가 명시되어 있다면, 속성을 생략해도 해당 Exception에 대해서만 동작한다.

여러 개의 Exception을 잡아야 하는 경우 `{IOException.class}`처럼 포괄적으로 적기보다는  `{FileSystemException.class, RemoteException.class}`처럼 구체적인 Exception을 적어주는 것이 좋다.

### @ControllerAdvice

`@ContollerAdvice`는 `@Controller`와 `handler`에서 발생하는 예외들을 모두 잡아준다.

<aside>
💡 handler?

> 핸들러(handler)는 컨트롤러 안에서 어떤 요청을 처리할 수 있는 메소드를 말한다.
> 

```java
@Controller 
public class UserController { 

//===핸들러==================== 
	@RequestMapping("/hello") 
	@ResponseBody 
	public String handler() { 
	  return "hello"; 
	} 
//=============================   
}
```

</aside>

`@ControllerAdvice`안에서 `@ExceptionHandler`를 사용하여 예외를 잡을 수 있다.

```java
@ControllerAdvice
public class SimpleControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> IllegalArgumentException() {
        return ResponseEntity.badRequest().build();
    }
}
```

**범위 설정**

`@ControllerAdvice`는 모든 예외를 잡아주기 때문에 일부 컨트롤러에서 발생한 예외만 처리하고 싶은 경우 따로 설정을 할 수 있다.

```java
// 1.
@ControllerAdvice(annotations = RestController.class)
public class ExampleAdvice1 {}

// 2.
@ControllerAdvice(basePackages = {"org.example.controllers"})
public class ExampleAdvice2 {}

// 3.
@ControllerAdvice(assignableTypes = {SimpleController.class})
public class ExampleAdvice3 {}
```

1. 어노테이션을 기준으로 구분한다.
위 예시에서는 `@RestContorller`를 사용한 컨트롤러의 예외만 처리한다.
2. 패키지를 기준으로 구분한다.
해당 패키지와 하위 패키지에서 발생한 예외를 모두 처리한다.
`basePackages`의 경우 패키지 이름으로 처리할 패키지를 지정하고,
`basePackagesClasses`의 경우 클래스를 기준으로 처리할 패키지를 지정한다.
3. 클래스를 기준으로 구분한다.
해당하는 클래스의 예외만 처리한다.

위 설정자는 컴파일 시점이 아닌 런타임 시 수행된다. 

때문에 너무 많은 설정자를 사용하면 성능이 떨어질 수 있다.

### @ControllerAdvice 우선 순위

`@ControllerAdvice` 컨트롤러가 여러 개 있을 시 @Order 어노테이션을 클래스 위에 작성하여 우선 순위를 지정해 줄 수 있다.

속성에는 다음 값을 넣어줄 수 있다.

1. int 값
→ 숫자가 낮을 수록 우선 순위가 높다.
    음수도 사용 가능하다.
2. Ordered.HIGHEST_PRECEDENCE
→ 가장 높은 우선 순위
3. Ordered.LOWEST_PRECEDENCE
→ 가장 낮은 우선 순위
    아무 것도 작성하지 않을 시 해당 값이 기본 값으로 사용된다.

`@ExceptionHandler` 간에도 우선 순위가 있을 수 있다.

<img width="1299" alt="image" src="https://github.com/inu-appcenter/server-study-16th/assets/86196038/d01055f0-33cd-4458-9edd-fb5ad461b9a0">

Exception.class는 최상위 클래스로 하위 세부 예외 처리 클래스로 설정한 Handler가 존재하면, 그 Handler가 우선 처리한다.

처리되지 못한 예외 처리에 대해서는 Exception.classd에서 핸들링한다.

<img width="923" alt="image 1" src="https://github.com/inu-appcenter/server-study-16th/assets/86196038/8b8d54d6-f0e6-4c7d-befd-836b868a60e6">

`@ExceptionHandler`가 각 Controller 안에 작성되는 경우도 있는데,
전역 설정(`@ControllerAdvice`)으로 설정한 Handler와 지역 설정(`@Controller`)로 정의한 Handler가 동일한 Exception을 처리할 경우 지역 설정의 Handler가 우선 순위를 갖는다.

### @RestControllerAdvice

`@RestControllerAdvice`는 `@ControllerAdvice`와 `@ResponseBody`을 가지고 있다.

즉 `@RestControllerAdvice`로 선언한 컨트롤러에서는 return 값이 응답 값의 body에 설정되어 클라이언트에게 전달된다.

`@RestControllerAdvice` 인터페이스는 다음과 같다.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ControllerAdvice
@ResponseBody
public @interface RestControllerAdvice {
	// ...
}
```

**@ControllerAdvice vs @RestControllerAdvice**

`@RestControllerAdvice`는 return 값을 바로 클라이언트에게 전달한다.

`@ControllerAdvice`는 return 값을 기준으로 동일한 이름의 view를 찾는다.