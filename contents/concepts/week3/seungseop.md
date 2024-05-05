# 3주차_객체의 직렬화와 역직렬화는 무엇일까요? /  @RequestMapping과 @ModelAttribute의 값 매핑은 어떻게 이뤄지나요?

생성일: 2024년 4월 15일 오후 2:56
태그: 서버 스터디

# 객체의 직렬화, 역직렬화

![img1 daumcdn](https://github.com/inu-appcenter/server-study-16th/assets/86196038/63e6a86c-f838-49fc-8ae3-c692468b3048)

> ***직렬화** : 객체에 저장된 데이터를 I/O 스트림에 쓰기(출력) 위해 연속적인(serial) 데이터로 변환하는 것*
> 
> 
> ***역직렬화** : I/O 스트림에서 데이터를 읽어서(입력) 객체를 만드는 것*
> 
> - *스트림?*
>     
>     <aside>
>     💡 ***스트림**은 클라이언트나 서버 간에 출발지 목적지로 입출력하기 위한 데이터가 흐르는 통로를 말한다.
>     자바는 스트림의 기본 단위를 바이트로 두고 있기 때문에, 네트워크, 데이터베이스로 전송하기 위해 최소 단위인 바이트 스트림으로 변환하여 처리한다.*
>     
>     </aside>
>     

### **자바 직렬화,** **역직렬화**

자바에서 직렬화와 역직렬화는 객체를 파일로 저장하거나 네트워크를 통해 전송하기 위해 제공되는 기능이다.

객체는 ‘인스턴스 변수의 집합’이므로 객체를 저장/전송하는 것은 객체의 인스턴스 변수의 값을 저장/전송하는 것과 동일하다.
**

시스템적으로 보면, JVM의 힙(heap) 혹은 스택(Stack) 메모리에 상주하고 있는 객체 데이터를 직렬화를 통해 바이트 형태로 변환하여 데이터베이스나 파일과 같은 외부 저장소에 저장해두고, 다른 컴퓨터에서 이 파일을 가져와 역직렬화를 통해 자바 객체로 변환해서 JVM 메모리에 적재한다고 보면 된다. 

**직렬화가 필요한 이유**

자바에는 원시타입(Primitive Type)이 byte, short, int, long, float, double, boolean, char 총 8가지가 있다. 

그리고 그 외의 객체들은 주소값을 갖는 참조형 타입이다.

![image-20230414201817935](https://github.com/inu-appcenter/server-study-16th/assets/86196038/0ed8adbf-0fae-4394-9425-5ca370757c67)

원시타입은 stack에서 값 그 자체로 갖고 있어 외부로 데이터를 전달할 때, **값을 일정한 형식의 raw byte 형태로 변경하여 전달할 수 있다.** 

하지만, 위 그림과 같이 객체의 경우 실제로는 **Heap 영역에 존재하고 스택에서 Heap 영역에 존재하는 객체의 주소 (메모리 주소)를 갖고 있다.**

이 주소 값을 그대로 다른 곳에 보낸다고 하자.

만약 프로그램이 종료되거나 객체가 쓸모없다고 판단되면 Heap 영역에 있던 데이터는 제거된다. 즉, 메모리에서 데이터가 삭제된다.

외부로 전송했을 때에도, 전송받은 기기의 전달받은 메모리 주소에 전송하려했던 데이터는 존재하지 않는다. 

따라서 이 주소 값의 데이터(실체)를 Primitive한 값 형식의 데이터로 변환하는 작업을 거친 후 전달해야 하며 직렬화를 통해 이를 해결할 수 있다.

### **자바 직렬화, 역직렬화 방법**

자바에서는 객체의 직렬화, 역직렬화를 위해 ObjectInputStream과 ObjectOutputStream을 제공한다.

이는 객체를 스트림에 쓰거나(출력) 읽는 기능(입력)을 제공하는 보조 스트림으로

ObjectOutputStream은 직렬화, 스트림에 객체를 출력하기 위해 사용하고

ObjectInputStream은 역직렬화, 스트림으로부터 객체를 입력 받기 위해 사용한다.

간단한 예제를 보면

```java
public class Member implements Serializable {
    
    String name;
    transient String password;  // 직렬화 대상에서 제외되어 직렬화 시 null 처리
}
```

먼저 직렬화 하고자 하는 객체는 java.io.Serializable 인터페이스를 구현한 클래스여야 한다. Serializable 인터페이스는 기능이 없고 단순히 객체 직렬화를 명시하는 용도의 마크 인터페이스이다.

또한 직렬화에서 제외하고 싶은 대상은 transient를 붙여 제외할 수 있다. transient 필드는 null(참조형) 또는 타입의 기본값으로 반환된다.

```java
// 객체를 직렬화하여 파일에 저장
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("objectfile.ser"));
oos.writeObject(new Member());

// 파일로부터 객체를 읽어 객체 생성
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("objectfile.ser"));
Member member = (Member) ois.readObject();  // Object로 리턴되므로 명시적 형변환 필요
```

Serializable로 마크된 객체는 writeObject()를 통해 직렬화를 하고 readObject()를 통해 역직렬화를 수행할 수 있다.

### **JSON 직렬화, 역직렬화**

직전에 설명한 자바 직렬화/역직렬화는 JVM과 ObjectOutputStream/ObjectIntputStream에 위임하여 바이트 형태와 전환하는 방식이었지만 XML, JSON과 같은 포맷으로의 직렬화도 가능하다. 

이를 가능하게 하는 JSON 라이브러리는 여러가지가 있다.

- Jackson
- Google-gson
- JSON-lib
- Flexjson
- Json-io
- Genson
- JSONij

이 중 가장 잘 알려져 있으며 대표적인 라이브러리는 Jackson이다.

Jackson 라이브러리에서는 ObjectMapper를 사용해 Object - JSON의 직렬화, 역직렬화를 수행한다.

ObjectMapper에 대해서는 뒤에서 설명하겠다.

### **자바 직렬화 vs JSON**

JSON은 웹(Web) 뿐만 게임 쪽에서도 설정 파일로 쓰이거나 데이터를 교환 할 때 범용적으로 사용된다. 

실제로 바이트로 직렬화가 오로지 자바 프로그램에서만 사용이 가능한 것과 달리, JSON 형태로 객체 데이터를 저장해두면 파이썬, 자바스크립트에서도 범용적으로 사용이 가능하다.

때문에 JSON으로의 직렬화가 가능하다면 굳이 바이트로 변환하는 자바 직렬화가 필요한지 의문이 들 수 있다.

하지만 자바 직렬화도 장점이 있다.

**자바 직렬화의 장점**

1. 직렬화는 자바의 고유 기술인 만큼 자바 시스템에서 개발에 최적화 되어있다.
2. 자바의 광활한 레퍼런스 타입에 대해 제약 없이 외부에 내보낼 수 있다.

기본형(int, double, string) 타입이나 배열(array)와 같은 타입들은 왠만한 프로그래밍 언어가 공통적으로 사용하는 타입이기 때문에, 이러한 값들은 JSON만으로도 충분히 상호 이용이 가능하다.

하지만 자바의 온갖 컬렉션이나 클래스, 인터페이스 타입 혹은 사용자가 생성한 커스텀 자료형 타입은 외부에 내보내기 위해선 각 데이터를 매칭시키는 별도의 파싱이 필요하다.

그에 반해 직렬화를 이용하면 비록 파이썬이나 자바스크립트와 같은 다른 시스템에서는 사용하지 못할지라도, 직렬화 기본 조건만 지킨다면 하드한 작업 없이 바로 외부에 보낼 수가 있다. 

그리고 역직렬화를 통해 읽어 들이면 데이터 타입이 자동으로 맞춰지기 때문에 별도의 파싱 없이 자바 클래스의 기능들을 곧바로 다시 이용할 수 있다. 그래서 직렬화된 문자열을 데이터베이스에 저장해두고 꺼내쓰기도 한다.

**자바 직렬화의 문제점**

위에서는 장점을 이야기 했지만 사실 직렬화는 장점보다는 단점이 극명하게 많다. 때문에 자바 직렬화의 문제점을 간단하게 살펴보겠다.

- 직렬화는 용량이 크다
    - 직렬화는 객체의 데이터 뿐 아니라 타입 정보, 클래스 메타 정보도 갖고 있으므로 용량을 많이 차지한다.
    JSON과 비교하자면 파일 용량 크기가 거의 2배 이상 차이가 난다.
- 역직렬화는 위험하다
    - 직렬화 설정 자체는 문제 없지만, 남이 만든 것을 역직렬화 하는 과정에서 나도 모르게 공격 당할 위험성이 있다.
    바이트 스트림을 역직렬화 하는 ObjectInputStream의 readObject() 메서드를 호출하게 되면 객체 그래프가 역직렬화 되어 classpath 안의 모든 타입의 객체를 만들어 내게 되는데, 해당 타입 객체 안의 모든 코드를 수행할 수 있게 되므로 나의 프로그램 코드 전체가 공격 범위에 들어가게 된다.
    - 또는 객체를 직렬화 하여 외부로 전송하는 과정에서 중간에 누가 가로채 파일 바이트 내용을 조작하면, 송신자가 역직렬화 하는 과정에서 인스턴스에 위험한 값을 대입시켜 불변을 깨는 식으로의 공격이 가능해진다.
    이는 역직렬화는 생성자 없이 인스턴스화가 가능하기 때문이다.
- 릴리즈 후에 수정이 어렵다
    - 클래스가 Serializable을 구현하게 되면 직렬화 된 바이트 스트림 인코딩도 하나의 공개 API가 되는 것이다.
    직렬화를 구현한 클래스가 널리 퍼지면 직렬화 형태도 영원히 지원해야하기 때문에 객체의 유지보수가 직렬화에 묶이게 된다.
- 클래스 캡슐화가 깨진다
    - 만약 직렬화할 클래스에 private 멤버가 있어도 직렬화를 하게 되면 그대로 외부로 노출되게 된다. 
    직렬화를 제외하려면 별도로 transient 설정을 해야한다.
    따라서 Serializable을 구현하면 직렬화 형태가 하나의 공개 API가 되어 캡슐화가 깨진다.
- 버그와 보안에 취약하다
    - 자바에서는 객체를 생성자를 이용해 만드는 것이 기본이다. 
    하지만 역직렬화는 언어의 기본 매커니즘을 우회하여 객체를 바로 생성하도록 한다. 
    만약 어느 객체가 생성자를 통해 인스턴스화 할 때 불변식이나 허가되지 않는 접근을 설정하였을 경우, 이를 무시하고 생성된다는 문제가 있다.
    다만 이 부분은 역직렬화 방어 기법 중 하나인 직렬화 프록시 패턴으로 극복할 수 있다.
- 새로운 버전을 릴리즈할 때 테스트 요소가 많아진다
    - 만일 직렬화 가능한 클래스가 업데이트 되면 구버전의 직렬화 형태가 신버전에서 역직렬화가 가능한지 테스트 해야 한다. 
    즉, 테스트의 양이 직렬화 가능 클래스의 수와 릴리즈 횟수에 비례하게 된다.
- 상속용 클래스와 인터페이스에 직렬화 구현을 주의해야 한다.
    - 상속 목적으로 설계된 클래스와 인터페이스를 Serializable로 구현한다는 것은, 위에서 언급한 자바 직렬화의 위험성을 고스란히 하위 클래스에 전이하게 되는 것과 다름이 없다.
- 내부 클래스는 직렬화를 구현하면 안된다
    - 내부 클래스 (inner class)의 직렬화 형태는 불분명하여 Serializable을 구현하면 안된다.
    단, 정적 내부 클래스 (static inner class)는 Serializable을 구현해도 상관 없다.

이처럼 자바 직렬화에는 많은 단점과 위험 요소가 존재한다. 

때문에 가급적 JSON 등의 데이터 표현을 사용하는 방식을 채택하는 것이 좋을 것이다.

하지만 직렬화는 1997년에 탄생하여 여전히 자바 생태계 곳곳에 쓰이고 있다. 만약 어쩔 수 없이 Serializable을 구현해야 한다면 위의 문제점을 잘 고려하여 설계해야 할 것이다.

# 스프링에서 직렬화, 역직렬화

스프링에서 객체 데이터를 JSON으로 주고 받을 때 많이 사용하는 방법에는 ObjectMapper와 @RequestBody, @ResponseBody가 있고 @RequestBody, @ResponseBody는 내부적으로 ObjectMapper를 사용한다.

스프링 부트에서는 기본적으로 Jackson이 내장되어 있고, 위에서 말했 듯 Jackson 내부에 ObjectMapper 클래스가 있다. 

즉, 스프링에서 직렬화, 역직렬화를 알려면 ObjectMapper에 대해서 알아야 한다.

### **ObjectMapper**

![img1 daumcdn 1](https://github.com/inu-appcenter/server-study-16th/assets/86196038/ba9c156c-0556-4d70-a7bf-de183a3eee85)

> ***ObjectMapper**란 Java Object와 JSON 데이터 간 형태를 자유롭게 변경하기 위해 사용하는 기술이다.*
> 

ObjectMapper는 POJO, Plain Old Java Object를 기준으로 동작한다. POJO는 기본 생성자, getter, setter를 가진 자바 클래스를 의미한다. 

그래서 XXX라는 필드가 있다면 직렬화 시 `getXXX` 메서드로 필드 값을 읽어서 JSON으로 구축하고,
역직렬화 시 기본 생성자로 객체를 생성한 뒤 `setXXX` 메서드로 필드에 값을 주입하게 된다.

더 자세히 보자면 ObjectMapper는 기본적으로 public 필드를 대상으로 삼는다.
public으로 선언한 필드는 자동으로 매핑이 되지만 통상 클래스 선언 시에는 필드를 private으로 선언하기 때문에 해당 필드들을 매핑하기 위해 getter 혹은 setter를 사용한다.
이 때, ObjectMapper는 getter와 setter의 메서드명 중 get, set을 제거한 이름의 첫 문자를 소문자로 치환하여 필드명을 유추한다.
getter와 setter는 private 필드를 인식하게 해주지만 직렬화의 경우 setter만으로는 private 필드를 인식할 수가 없다. 이는 getter를 가진 private 필드는 property로 간주되지만 setter는 그렇지 않기 때문이다.

반대로 역직렬화의 경우에는 setter만 있어도 private 필드를 인식할 수 있고 값을 주입할 수도 있다.

그러나 객체의 불변성 보장을 위해 setter를 사용하지 않는 경우도 많다. 

이 경우 private 필드를 인식하여 필드명을 알아내는 데에는 getter를 사용하고, 데이터를 객체에 바인딩할 때에는 리플렉션의 필드 접근 방식을 사용하게 된다.


💡 ***리플렉션 (Reflection)** 은 구체적인 클래스 타입을 알지 못해도 그 클래스의 메소드, 타입, 변수들에 접근할 수 있도록 해주는 자바 API이다.*

여기서 중요한 것은 역직렬화의 경우 객체를 생성해야 하기 때문에 생성자가 필요한데, ObjectMapper는 기본 생성자를 사용해 객체를 생성하고 이후 값을 바인딩 할 때 setter 혹은 리플렉션을 사용한다는 점이다.

때문에 기본적으로는 다른 생성자가 있어도 @NoArgsContructor를 사용하거나 직접 기본 생성자를 만들어줘야 한다.

> ObjectMapper에서 인자를 가진 생성자가 아니라 기본 생성자를 통해 객체를 생성하는 이유가 리플렉션이 생성자의 파라미터 정보를 가져올 수 없기 때문이라는 이야기가 많은데, 이는 Java8 이전까지의 이야기이고 Java8 이후부터는 파라미터 정보도 가져올 수 있게 되었다. 
다만 기본 설정으로는 여전히 기본 생성자 없이 역직렬화는 불가능하고, 만약 기본 생성자를 생성하지 않고 역직렬화를 하고 싶다면 @jsonProperty를 사용하여 필드 정보를 명시해주면 가능하다.
> 

# 스프링의 요청 매핑

요청 매핑을 보기에 앞서 Controller 클래스에서 사용하는 몇 가지 어노테이션을 먼저 살펴보겠다.

- @ResponseBody
    - 반환 값이 String 이면 뷰 이름으로 인식해서 뷰를 찾고 뷰를 렌더링 하던 것과는 달리 @ResponseBody를 메서드 위에 붙이면 반환 값으로 뷰를 찾는 게 아니라, String을 HTTP 바디에 바로 입력한다.
- @RestController
    - @ResponseBody를 메서드 위에 각각 붙여 사용하는 대신 @RestController를 클래스 위에 붙여 사용한다.
- @RequestMapping("/xxx")
    - /xxx URL이 호출되면 해당 메서드를 호출한다.
    - 대부분의 속성을 배열[]로 제공하기 때문에 다중 설정도 가능하다.
    - 클래스 위에 붙여 공통으로 들어갈 경로를 설정 가능하다.
    - 메서드에서 사용될 때에는 축약 어노테이션을 사용해서 지정할 수 있다.
        
        > **@GetMapping**: GET 요청일 경우만 호출  (= @RequestMapping(value="", method = RequestMethod.GET)
        **@PostMapping**: Post 요청일 경우만 호출
        **@DeleteMapping**: Delete 요청일 경우만 호출
        **@PatchMapping**: Patch 요청일 경우만 호출
        **@PutMapping**: Put 요청일 경우만 호출
        > 

### 요청 매핑

- **@PathVariable 경로 변수**

```java
@GetMapping("/mapping/{userId}/{orderId}")
public String mappingPath(@PathVariable("userId") String userId, @PathVariable("orderId") String orderId) {
    log.info("mappingPath userId ={}, orderId={}", userId, orderId);

    return "Ok";
}
```

메소드 내부에 @PathVariable를 사용해 경로변수를 설정할 수 있다.

만약 @PathVariable 변수 이름과 매핑된 URL 변수 이름이 같다면 ****@PathVariable을 생략할 수 있다.

**@RequestMapping - 특정 파라미터 조건 매핑 (params)**

```java
/**
 * 파라미터로 추가 매핑
 * params="mode",
 * params="!mode"
 * params="mode=debug"
 * params="mode!=debug" (! = )
 * params = {"mode=debug","data=good"}
 */
@GetMapping(value = "/mapping-param", params = "mode=debug")
public String mappingParam() {
    log.info("mappingParam");
    return "ok";
}
```

특정 파리미터가 있거나 없는 조건을 추가할 수 있으나 잘 사용하진 않는다.

위 코드는 다음 요청 URL로 호출한다.

> localhost:8080/mapping-param?mode=debug
> 

**@RequestMapping - 특정 헤더 조건 매핑 (header)**

```java
/**
 *특정 헤더로 추가 매핑
 * headers="mode",
 * headers="!mode"
 * headers="mode=debug"
 * headers="mode!=debug" (! = )
 */
@GetMapping(value = "/mapping-header", headers = "mode=debug")
public String mappingHeader() {
    log.info("mappingHeader");
    return "ok";
}
```

파라미터와 유사하지만 HTTP 헤더를 사용한다.

**@RequestMapping - 헤더 기반 추가 매핑 Media Type (consumes)**

```java
/**
 * Content-Type 헤더 기반 추가 매핑 Media Type
 * consumes="application/json"
 * consumes="!application/json"
 * consumes="application/*"
 * consumes="*\/*"
 * MediaType.APPLICATION_JSON_VALUE
 */
@PostMapping(value = "/mapping-consume", consumes = "application/json")
public String mappingConsumes() {
    log.info("mappingConsumes");
    return "ok";
}
```

HTTP 요청의 Content-Type 헤더를 기반으로 미디어 타입으로 매핑한다.

만약 맞지 않으면 HTTP 415 상태코드(Unsupported Media Type)를 리턴한다.

**@RequestMapping - Accept 헤더 기반 Media Type 콘텐츠 네고시에이션 ( produces )**

```java
/**
 * Accept 헤더 기반 Media Type ( 콘텐츠 네고시에이션 )
 * produces = "text/html"
 * produces = "!text/html"
 * produces = "text/*"
 * produces = "* \/*"
*/
@PostMapping(value = "/mapping-produce", produces = "text/html")
public String mappingProduces() {
    log.info("mappingProduces");
    return "ok";
}
```

HTTP 요청의 Accept 헤더를 기반으로 미디어 타입으로 매핑한다.

만약 맞지 않으면 HTTP 406 상태코드(Not Acceptable)를 반환한다.

여기까지는 HttpServletRequest가 제공하는 방식으로 요청 파라미터를 조회하는 방식을 살펴보았고 이제부터 스프링으로 요청 파라미터를 조회하는 방법을 알아보겠다.

**@RequestParam**

```java
// @RequestParam 사용 - String, int, Integer 같은 단순 타입
@ResponseBody
@RequestMapping("/request-param")
public String requestParamV2(@RequestParam("username") String username,
                           @RequestParam("age") int age) throws IOException
{
    log.info("username = {}, age={}", username, age);
    return "OK";
}
```

@RequestParam 에 파라미터를 바인딩해서 요청 파라미터를 조회할 수 있다.

@RequestParam의 name(value) 속성이 파라미터 이름으로 사용된다.

> @RequestParam("username") String memberName 
→ request.getParameter("username")
> 

@PathVariable과 마찬가지로 @RequestParam에서도 변수이름과 value가 같으면 value를 생략할 수 있다.

또한 String, int, Integer와 같은 단순 타입일 경우에는 @RequestParam도 생략할 수 있다. 다만 명시적이지 않아 읽는 사람이 읽기 어려울 수 있어 권장하진 않는다.

@RequestParam은 속성을 통해 필수 파라미터와 기본값을 지정할 수 있다.

```java
@ResponseBody
@RequestMapping("/request-param-default")
public String requestParamDefault(
        @RequestParam(required = true, defaultValue = "guest") String username,
        @RequestParam(required = false, defaultValue = "1") Integer age) {
    log.info("username={}, age={}", username, age);
    return "OK";
}
```

required 옵션을 true로 설정한 파라미터는 요청 시 필수적으로 있어야 한다.

> 주의 : 빈 문자("")도 통과한다. → (?username=""&age=20)
> 

defaultValue 옵션을 설정한 파라미터는 요청 시 입력하지 않으면 defaultValue로 설정된다.

defaultValue를 true로 설정한 경우 required 옵션은 의미가 사라진다. ( 디폴트로 값이 설정되므로 )

> defaultValue는 빈 문자의 경우에도 설정한 기본 값이 적용된다.
> 

또한 @RequestParam은 Map, MultiMap을 통한 파라미터 조회가 가능하다.

```java
@ResponseBody
@RequestMapping("/request-param-map")
public String requestParamMap(@RequestParam Map<String,Object> paramMap)
{
    log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
    return "OK";
}

@ResponseBody
@RequestMapping("/request-param-Multimap")
public String requestParamMap(@RequestParam MultiValueMap<String, Object> paramMultiMap)
{
    log.info("username={}, age={}", paramMultiMap.get("username"), paramMultiMap.get("age"));
    return "OK";
}
```

요청 파라미터의 값이 1개가 확실하다면 Map 을 사용해도 되지만, 그렇지 않다면 MultiValueMap을 사용해야 한다.

**@ModelAttribute**

```java
@Data // @Setter, @Getter, @Tostring, @EqualsAndHashCode, @RequiredArgsConstructor
public class HelloData {
    private String username;
    private int age;
}
```

요청 파라미터를 바인딩한 객체가 위와 같을 때 다음 코드와 같이 @ModelAttribute를 통해 요청 파라미터를 받는다.

```java
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttributeV1(@ModelAttribute HelloData helloData)
{
    log.info("username = {} , age = {} ", helloData.getUsername(), helloData.getAge());
    log.info("data = {}",helloData);

    return "ok";
}
```

> 스프링 MVC에서는 @ModelAttribute가 있으면 다음과 같이 실행한다.
> 
> 1. HelloData 객체를 생성한다.
> 2. 요청 파라미터의 이름으로 HelloData 객체의 property (username, age)를 찾는다.
> 3. 해당 프로퍼티의 setter를 호출해 파라미터의 값을 입력(바인딩)하여 객체에 값을 주입한다.
> 
> ex) username -> setUsername() 메소드를 찾아 값 주입
> 
> ❗️바인딩 오류 : age=abc 와 같이 숫자가 들어가야 할 곳에 문자가 들어가면 BindException 예외가 발생
> 

@ModelAttribute 또한 생략 가능하다.
하지만 @RequestParam 도 생략 가능하기 때문에 혼란이 발생할 수 있어 권장하지는 않는다.

**단순 텍스트**

요청 파라미터와 다르게 HTTP 메시지 바디를 통해 데이터가 직접 넘어오는 경우에는 HTML Form 형식으로 전달되는 경우가 아니라면 @RequestParam과 @ModelAttribute를 사용할 수 없다. 

HTTP 메시지 바디의 데이터는 InputStream을 사용해 직접 읽을 수 있다.

```java
/**
 * @param inputStream    : HTTP 요청 메시지 바디의 내용을 직접 조회
 * @param responseWriter : HTTP 응답 메시지의 바디에 직접 결과 출력
 * @return
 * @throws IOException
 */
@PostMapping("/request-body-string-v2")
public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
    String messagebody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    log.info("messageBody = {}", messagebody);
    responseWriter.write("ok");
}
```

스프링 MVC는 다음 파라미터를 지원한다.

- InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
- OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력

HttpEntity를 통해서도 메시지 바디 조회가 가능하다

```java
 		/**
     * HttpEntity 패키지 사용
     * @param httpEntity
     * @return
     * @throws IOException
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {

        String messagebody = httpEntity.getBody();
        log.info("messageBody = {}", messagebody);

        return new HttpEntity<String>("ok");

//        return new ResponseEntity<>("ok", HttpStatus.CREATED);
// RequestEntity, ResponseEntity는 HttpEntity를 상속받은 객체로 
// RequestEntity는 HttpMethod, url 정보가 추가되어 , request에서 사용
// ResponseEntity는 HTTP 상태코드를 설정할 수 있고, response에서 사용
    }
```

HttpEntity 는 HTTP 헤더, body 정보를 편리하게 조회할 수 있도록 도와주는 객체이다.

요청 뿐만 아니라 응답에서도 HttpEntity를 사용할 수 있다.

스프링 MVC는 다음 파라미터를 지원한다.

- HttpEntity: HTTP header, body 정보를 편리하게 조회
    - 메시지 바디 정보를 직접 조회
    - 요청 파라미터를 조회하는 기능과 관계 없음 @RequestParam X, @ModelAttribute X
- HttpEntity는 응답에도 사용 가능
    - 메시지 바디 정보 직접 반환
    - 헤더 정보 포함 가능
    - view 조회 X
    

또한 @RequestBody를 사용한 메시지 바디 조회도 가능하다.

```java
/**
 * @RequestBody 사용 - 실무에서 가장 많이 사용하는 방식
 * @param messageBody
 * @return
 */
@ResponseBody
@PostMapping("/request-body-string-v4")
public String requestBodyStringV4(@RequestBody String messageBody)
{
    log.info("message body = {}",messageBody);
    return "OK";
}
```

만약 헤더 정보가 필요하다면 HttpEntity를 사용하거나 @RequestHeader를 사용하면 된다.

💡 이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 @RequestParam, @ModelAttribute와는 전혀 관계가 없다.

요청 파라미터 조회 : @RequestParam, @ModelAttribute

메시지 바디 조회 : @RequestBody

**JSON**

단순 텍스트형식의 메시지바디와 Json형식의 메시지 바디 조회는 크게 다르지 않다.

```java
@ResponseBody
@PostMapping("/request-body-json-v2")
public String requestBodyJsonV2(@RequestBody String messagebody) throws IOException
{
    HelloData data = objectMapper.readValue(messagebody, HelloData.class);
    log.info("username = {} , age = {}", data.getUsername(), data.getAge());
    return "Ok";
}
```

단순 텍스트처럼 @RequestBody를 사용해 HTTP 메시지에서 데이터를 꺼내고 messagebody에 저장한다.

문자로 된 JSON 데이터인 messagebody를 objectMapper를 통해 자바 객체로 변환한다.

💡 ❗️@RequestBody는 생략이 불가능하다.

스프링은 @ModelAttribute, @RequestParam과 같은 어노테이션을 생략할 때 아래 규칙을 적용한다.

- String , Integer, int 와 같은 단순 타입 = @RequestParam
- 그 외의 타입 = @ModelAttribute ( argument resolver로 지정해둔 타입을 제외한 모든 타입)

따라서 이 경우 HelloData에 @RequestBody를 생략하면 @ModelAttribute가 적용되어 버리기 때문에 메시지 바디가 아닌 요청 파라미터를 처리하게 된다.


@RequestBody에 String이 아닌 객체 파라미터를 사용할 수도 있다.

```java
// @RequestBody에 객체를 파라미터로 지정할 수 있다.
@ResponseBody
@PostMapping("/request-body-json-v3")
public String requestBodyJsonV3(@RequestBody HelloData data)
{
    log.info("username = {}, age = {}",data.getUsername(),data.getAge());
    return "OK";
}
```

@RequestBody나 HttpEntity를 사용하면 HTTP 메시지 컨버터가 HTTP 메시지 바디의 내용을 우리가 원하는 문자나 객체 등으로 변환해 준다.


💡 HTTP 메시지 컨버터는 내부에서 ObjectMapper를 호출한다.


아래는 HttpEntity를 사용한 코드이다.

```java
@ResponseBody
@PostMapping("/request-body-json-v4")
public String requestBodyJsonV4(HttpEntity<HelloData> dataHttpEntity)
{
    HelloData data = dataHttpEntity.getBody();
    log.info("username = {}, age = {}",data.getUsername(),data.getAge());
    return "OK";
}
```

요청 매핑은 아니지만 @ResponseBody에 String이 아닌 객체를 리턴할 경우도 추가로 살펴보자면

```java
@ResponseBody
@PostMapping("/request-body-json-v5")
public HelloData requestBodyJsonV5(@RequestBody HelloData data)
{
    log.info("username = {}, age = {}", data.getUsername(),data.getAge());
    return data;
}
```

위에서 살펴보았 듯 스프링에 내장된 Jackson의 ObjectMapper 클래스가 객체 정보를 JSON 형태로 변환 시켜 메시지 바디에 리턴한다. 

이것 또한 마찬가지로 HttpEntity를 사용해도 된다.

> @RequestBody 요청 : JSON 요청 -> HTTP 메시지 컨버터 -> 객체
> 
> 
> @ResponseBody 응답 : 객체 -> HTTP 메시지 컨버터 -> JSON 응답
>