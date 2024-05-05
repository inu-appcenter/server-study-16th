## 예외처리의 개념

### 오류(Error)와 예외(Exception) 의 차이는?

---

오류 : 시스템 레벨에서 발생하는 프로그램 코드로 해결될 수 없는 심각한 문제를 의미한다. 

대부분의 에러는 자바의 경우, JVM에서 발생하며 메모리부족(OutMemoryError)이나 스택 오버플로(StackOverFlowError)와 같은 비정상적인 상황에서 발생한다. 

**이러한 에러는 시스템에 심각한 문제가 있다는 신호로 알 수 있으며, 코드로 대응을 하기 보다는 문제의 원인을 찾아서 해결하는 것이 중요하다.**

예외 :  프로그램 실행 중 발생할 수 있는 예외적인 조건을 의미하며, 개발자가 코드 내에서 적절히 처리할 수 있다. 

예외는 또한 체크된 예외(checked exceptions) 와 체크되지 않은 예외(unchecked exceptions)으로 나뉘고, 각각 다른 방식으로 처리된다. 

**예외는 개발자가 구현한 로직에서 발생한 실수나 사용자의 영향에 의해 발생하므로 이를 미리 예측하여 방지할 수 있도록 상황에 맞는 예외 처리를 하는 것이 중요하다.**

오류와 예외의 차이는 **개발자가 코드로 해결할 수 있는지 없는지의 문제**이다. 
오류는 대부분 시스템에 의해 발생하는 비가역적인 심각한 문제로, 개발자가 코드로 해결할 수 없는 문제들인 반면, 예외는 코드 실행 중에 발생하는 예측가능하고, 대응이 가능한 문제이다. 따라서, 개발자가 예상하고 적절히 예외를 처리할 수 있는 문제들이다. 

### 이들을 구분하는 이유는?

프로그램을 개발하고 유지보수하는 과정에서 예상치 못한 상황에 대처하고, 시스템의 안정성을 확보하기 위함이다. 

에러는 심각한 문제로 인식하여 시스템적인 차원에서 개입이 필요한 반면, 예외는 처리 가능한 문제이기 때문에 코드를 통한 예외처리로 프로그램의 견고함을 증진시킬 수 있다. 

예외 처리(Exception Handling)는 효율적인 방법으로 프로그램의 안정성을 높이고 사용자에게 명확한 피드백을 제공하는 메커니즘을 마련하는 것을 목표로 한다.

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/4275b07c-5390-489c-8bbf-82e4caa3dbbc/214e04ce-af62-4174-a6ab-ecf4540ad407/Untitled.png)

예외와 오류 모두 Java에서의 최상위 객체인 Object클래스를 상속받고, 그 사이 Throwable 클래스와도 상속관계를 가진다. 이 클래스는 **오류나 예외에 대한 메시지를 담는다.** 

이 Throwable 객체가 가진 정보와 할 수 있는 행위는 getMessage()와 printStackTrace()라는 메서드로 구현되어 있다. 이는 예외가 연결될 때 연결된 예외의 정보들을 기록하는 기능도 있다. 

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/4275b07c-5390-489c-8bbf-82e4caa3dbbc/9d088c0e-6770-48e5-b8b2-cb59dd7d19f6/Untitled.png)

[Error] 

✔️ OutOfMemoryError 

 : JVM이 할당된 메모리의 부족으로 더이상 객체를 할당할 수 없을 때 던져지는오류이다. 

- heap 사이즈 부족
- 너무 많은 클래스 로드
- 가용가능한 swap이 없을시
- GC에 의해 추가적인 메모리가 확보되지 못하는 상황일 때

✔️ StackOverFlowError

: 응용프로그램이 너무 많이 반복되어 stackoverflow가 발생할 때 던져지는 오류 

다만, 간접적인 예방은 가능 

stackoverflow :  재귀조심, 가시적인 loop사용…

outofmemory : 메모리 차단, heap크기 늘려줌…

와 같이 전체적인 코드와 시스템적으로 봐야함. 

[Exception] 예외

체크되지 않은 예외의 대표적인 예로는 NullPointException, ArrayIndexOutBoundsException와 같은 오류를 가리킨다. 체크된 예외의 경우에는 IOException와 같은 오류로, 반드시 예외 처리를 해야하는 경우이다. 

✏️ Checked Exception  VS Unchecked Exception 

- Checked Exception
    - 예외처리가 필수적이며, 처리하지 않으면 컴파일 되지 않는다.
    - `try-catch`로 감싸거나, `throw`로 던져서 예외처리를 해야한다.
    - 컴파일 단계에서 명확하게 exception체크가 가능하다.

- Unchecked Exception
    - RuntimeException 하위의 모든 예외
    - NullPointerException , ArrayIndexOutofBoundsException 등등
    - 실행과정중 어떤 특정한 논리에 의해 발견되는 Exception
    - 명시적인 예외처리를 하지 않아도 된다.

## 예외 처리의 방법 - 예외 복구/예외 처리 회피/예외 전환

---

- 예외 복구
    
    예외 상황을 파악하고 문제를 해결하여 정상상태로 돌려놓는 방법이다. 
    
    try~catch문
    
    ```java
    final int MAX_RETRY = 100;
    public Object someMethod() {
        int maxRetry = MAX_RETRY;
        while(maxRetry > 0) {
            try {
                ...
            } catch(SomeException e) {
                // 로그 출력. 정해진 시간만큼 대기한다.
            } finally {
                // 리소스 반납 및 정리 작업
            }
        }
        // 최대 재시도 횟수를 넘기면 직접 예외를 발생시킨다.
        throw new RetryFailedException();
    }
    
    // while 문에서 예외를 잡아서 대기하거나, 재시도를 통하여 문제를 해결한다.
    // 정해놓은 횟수나 시간을 넘기면 예외를 발생시킨다.
    ```
    
- 예외 처리 회피
    
    예외 처리를 직접 담당하지 않고 호출한 메서드로 위임해 회피하는 방법이다. 
    
    throws문
    
    ```java
    // Example 1
    public void add() throws SQLException {
        // ...생략
    }
    
    // Example 2 
    public void add() throws SQLException {
        try {
            // ... 생략
        } catch(SQLException e) {
            // 로그를 출력하고 다시 날린다!
            throw e;
        }
    }
    
    // 예시 1처럼 바로 위임해서 던질 수 있지만, 
    // 긴밀한 관계를 가진 것이 아니라면, 예시 2처럼 어느정도 처리하고 던지는 것이 좋다.
    ```
    
- 예외 전환
    
    예외 회피와 비슷하게 메서드 밖으로 예외를 위임하지만, 그냥 위임하지 않고 적절한 예외로 전환해서 넘기는 방법이다. 
    
    ```java
    // 조금 더 명확한 예외로 던진다.
    public void add(User user) throws DuplicateUserIdException, SQLException {
        try {
            // ...생략
        } catch(SQLException e) {
            if(e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                throw DuplicateUserIdException();
            }
            else throw e;
        }
    }
    
    // 예외를 단순하게 포장한다.
    public void someMethod() {
        try {
            // ...생략
        }
        catch(NamingException ne) {
            throw new EJBException(ne);
            }
        catch(SQLException se) {
            throw new EJBException(se);
            }
        catch(RemoteException re) {
            throw new EJBException(re);
            }
    }
    
    // 조금 더 명확한 의미로 전달되기 위해 적합한 의미를 가진 예외로 변경하여 던진다. (사용자 정의 가능) 
    // 예시 2처럼 단순하게 만들어서 각 예외별로 포장(wrap)하여 예외를 처리할 수 있다. 
    ```
    

## 자바의 예외 클래스는? - Checked Exception / Unchecked Exception

---

앞서 설명했지만, 클래스별로 자세하게 알아보자. 

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/4275b07c-5390-489c-8bbf-82e4caa3dbbc/6b45dc95-7fcc-4651-aff5-251a2b3f83ee/Untitled.png)

|  | Checked Exception | Unchecked Exception |
| --- | --- | --- |
| 처리 여부 | 반드시 예외를 처리해야 함  | 명시적인 처리를 안해도 됨  |
| 확인 시점  | 컴파일 단계 | 런타임 단계 |
| 예외 종류 | RuntimeException을 제외한, Exception 클래스와 그를 상속받는 하위 예외                                                                                                     IOException FileNotFoundException SQLException
          | RuntimeException 과 그 하위 예외NullPointerException- IllegalArgumentException- IndexOutOfBoundException- SystemException  |

