# :deciduous_tree: 유효성 검사?

사용자로부터 입력된 데이터가 조건에 충족하는지 즉, 유효한지 검사하는 것

> :question: **유효성 검사를 왜 할까?** > <br> :arrow_forward: 올바르지 않은 데이터를 서버 또는 DB로 전송되는 것을 막기 위함
> <br> :arrow_forward: DB는 데이터의 무결성을 지켜야 함
> <br> :arrow_forward: 항상 일정한 형태의 데이터를 사용자에게 제공 가능

## :evergreen_tree: 유효성 검사 위치 in Spring

유효성 검사의 위치에 명확하게 정해진 기준은 없다. 상황에 따라 적절한 검사를 하면 된다. <br>
Client와 Server로 먼저 살펴보겠다.
Client와 Server 모두 유효성 검사 가능하다. 하지만 Client에서만 한다면 사용자가 개발자 도구 등을 사용해 코드를 변경시켜 유효성 검사를 넘기는 상황 발생 가능성이 있다. 또한 Client는 DB를 통한 유효성 검사를 할 수 없다.<br>
Server는 안정적 유효성 검사 및 DB를 통한 유효성 검사가 가능하다. Client의 몫까지 다 할 수 있지만 검사 속도가 느려진다. 결과적으로 적절하게 Client와 Server에 유효성 검사 분배를 해야 한다. <br>

## :evergreen_tree: @Valid vs @Validated

**@Valid**<br>

- 자바 표준 스펙
- 객체에 대해 검사
- Controller에서만 유효성 검사

Front Controller인 DispatcherServlet에 모든 요청이 전달 -> 전달 과정에서 ArgumentResolver가 동작해서 처리해줌 <br>
검사 실패시 MethodArgumentNotValidException 예외 발생 (DispatcherServlet에 기본으로 등록된 Exception Resolver인 DefaultHandlerExceptionResolver에 의해 400 Bad Request 에러 발생)

**@Validated** <br>

- Spring FrameWork에서 제공하는 어노테이션 (JSP 표준 기술 X)
- Controller가 아닌 다른 계층에서도 유효성 검사 가능

클래스에 유효성 검사를 위한 AOP 어드바이스 또는 인터셉터가 등록됨 -> 해당 클래스의 메소드가 호출될 때 AOP의 PointCut으로써 요청을 가로채 유효성 검증을 해줌
<br>
AOP 기반으로 동작하기 때문에 메소드의 요청을 가로채서 유효성 검사를 진행한다. <br>
검사 실패시 ContraintViolationException 예외 발생


## :evergreen_tree: 유효성 검사를 위한 어노테이션

유효성 검사를 할 객체의 각 필드에 원하는 어노테이션을 추가하면 됨 <br>

- @NotNull : 해당 값이 null이 아닌지 검증

- @NotEmpty :@NotNull + ""(빈 스트링) 이 아닌지 검증
- @NotBlank : @NotNull + 공백("", " ") 이 아닌지 검증
- @Email
- @Size : 
- @Min : 
- @Max :
- Pattern : 해당 값이 설정한 패턴과 일치하는지 검증 (정규표현식 사용)