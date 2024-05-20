# 📌 커스텀 예외 처리

&nbsp;
## 커스텀 예외를 사용하는 이유?

자바가 기본으로 제공하는 Standard Exception으로는 표현하지 못하는 이점이나 기능들을 제공하기 위해서이다. 

예를 들면, 예외의 이름을 통해 정보를 전달할 수 있고, 더 상세한 정보를 제공할 수 도 있다.

&nbsp;
## 커스텀 예외를 활용한 예외 처리 - 상속받아야 할 Exception class는?

웹 서비스 애플리케이션에서는 외부에서 들어오는 요청에 담긴 데이터를 처리하는 경우가 잦음

그 과정에서 예외 발생 시 클라이언트에 어떤 문제가 발생했는 지 상황을 전달하는 경우가 일반적이다.

따라서 주로 프로그램 실행 중에 확인 가능한 RuntimeException을 상속한다.

&nbsp;
## 커스텀 예외의 4가지 Best Practices

&nbsp;
## 1. Always Provide a Benefit (항상 혜택을 제공하라)

자바 표준 예외들에는 포함되어 있는 다양한 장점을 가지는 기능이 있다.

커스텀 예외의 의도는 자바의 표준 예외들로 표현할 수 없는 정보나 기능을 제공하는 것이다.

만약 위의 의도가 없다면 어떠한 장점도 제공할 수 없는 커스텀 예외를 만드는것 보다 오히려

표준 예외 중 하나를 사용하는 것이 낫다.

---

&nbsp;
## 2. Follow the Naming Convention (네이밍 컨벤션을 따라가라)

JDK가 제공하는 예외 클래스들을 보면 클래스의 이름들이 모두 "Exception"으로 끝난다는 것을 알 수 있다.

일반적인 네이밍 규칙으로 자바 생태계 전체에 적용되는 규칙이다.

따라서, 커스텀 예외도 해당 네이밍 규칙을 따르는 것이 좋습니다.

---

&nbsp;
## 3. Provide Javadoc Comments for Your Exception Class (예외 클래스에 대한 Javadoc 주석제공)

예외 클래스들은 API에서 외부적으로는 크게 드러나지 않는 부분일 수 있지만 

API 메소드가 어떤 하나의 예외를 기술 하고 있다면, 그 예외는 API의 한부분이 되는 것이고 문서화 해야한다.

커스텀 예외의 Javadoc에는 예외가 발생할 수도 있는 상황과 예외의 일반적인 의미를 기술한다.

해당 문서의 목적은 다른 개발자들이 우리의 API를 이해하도록 하고 예외 상황을 피하도록 돕기 위함 이다.

```java
/**
 * The MyBusinessException wraps all checked standard Java exception and enriches them with a custom error code.
 * You can use this code to retrieve localized error messages and to link to our online documentation.
 *
 * @author TJanssen
 */public class MyBusinessException extends Exception { ... }
```

---

&nbsp;
## 4. Provide Constructor That Sets the Cause(원인을 설정한 생성자를 제공)

보통 커스텀 예외를 사용할 때 자주 사용하는 형식은 표준 예외를 캐치해서 커스텀 예외로 전환해서 던지는 경우가 많다.

![image](https://github.com/inu-appcenter/server-study-16th/assets/62889359/b22fcf84-8c49-4e02-8964-a39c4fb49171)


Exception 과 RuntimeException 은 예외의 원인을 기술하고 있는 Throwable을 받을 수 있는 생성자 메서드를 제공한다.

커스텀 예외도 이렇게 생성자에서 예외의 원인을 인자로 받는것이 좋다.

```java
public class MyBusinessException extends Exception {
    public MyBusinessException(String message, Throwable cause, ErrorCode code) {
            super(message, cause);
            this.code = code;
        }
        ...
}
```
