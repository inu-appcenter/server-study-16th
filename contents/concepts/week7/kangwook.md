# 📌 JWT란 무엇인가요?

JWT란 Json포멧을 이용하여 사용자에 대한 정보를 저장하는 Claim기반의 Web Token이다. 

JWT는 Header,Payload,Signature의 구조로 이루어져 있고 각 부분은 인코딩 된 상태로 표현된다. 또한 각 부분을 구분하기 위해 점으로 구분하여 연결해준다.

![image](https://github.com/inu-appcenter/server-study-16th/assets/62889359/c14b3e54-73f8-4ebe-a732-a275c4d94260)

&nbsp;
- 헤더(header)

헤더에는 alg,typ 이 두 가지 정보로 구성된다. 

typ은 토큰의 타입을 지정하고,

alg는 해싱 암호화 알고리즘 방식을 지정한다.(signature을 해싱하기 위한 알고리즘)

```
{
   "alg": "HS256",
   "typ": JWT
 }
```

&nbsp;
- 페이로드(payload)

토큰의 페이로드에는 토큰에서 사용할 정보의 조각들인 **클레임**(Claim)이 담겨있다.

클레임은 **등록된 클레임(Registered Claim)**, **공개 클레임(Public Claim)**, **비공개 클레임(Private Claim)** 으로 나누어지며, key-value 형태로 존재한다.

- **등록된 클레임(Registered Claim)**

등록된 클레임은 **토큰 정보를 표현하기 위해 이미 정해진 종류의 데이터**들로, 모두 선택적으로 작성이 가능하며 사용할 것을 권장한다.

- iss: 토큰 발급자(issuer)
- sub: 토큰 제목(subject), unique한 값을 사용한다. 주로 사용자 이메일 사용
- aud: 토큰 대상자(audience)
- exp: 토큰 만료 시간(expiration), NumericDate 형식으로 되어 있어야 함 ex) 1480849147370
- nbf: 토큰 활성 날짜(not before)
- iat: 토큰 발급 시간(issued at), 토큰 발급 이후의 경과 시간
- jti: JWT 토큰 식별자(JWT ID), 중복 방지를 위해 사용하며, 일회용 토큰(Access Token) 등에 사용

&nbsp;
- 시그니처(signature)

Signature에는 유효성 검증을 할때 사용하는 암호화 코드를 저장한다.

헤더와 페이로드의 값을 인코딩한 값을 secret key를 이용해 정의한 알고리즘으로 해싱하고, 이 값을 다시 인코딩하여 생성한 값이 저장된다.

![image](https://github.com/inu-appcenter/server-study-16th/assets/62889359/e45a8661-7fa4-41d8-8d79-f84a7cc1b53f)

&nbsp;
- base64로 인코딩을 해서 토큰을 전달하는 이유(내 생각):

컴퓨터가 데이터를 처리할 때 가장 기본적인 데이터 단위는 바이트 코드이다.

해시 암호화 알고리즘을 수행할 때 문자열이 아닌 바이트 코드로 변환하여 수행하게 되는데,

반환값이 원래는 바이너리 데이터인데 대신 바이너리 데이터를  base64로 인코딩한 문자열을 반환한다.

base64는 바이너리 데이터를 문자열로 바꾸는 역할을 하기 때문에,

base64를 사용해 토큰을 전달한다. 일반적인 인코딩은 문자를 바이트 코드로 변환한다.

 

- 해시 함수 입력값들을 base64로 인코딩 하는 이유(내 생각):

해시 암호화는 복호화가 불가능해, 값을 똑같이 넣고 반환 값이 같은 걸로 검증이 가능하다.

근데 해시 함수는 입력값이 바이트 코드여야하기 떄문에, 인코딩을 한 뒤 값을 넣어야한다.

하지만 OS마다 기본 인코딩 방식이 다를 수가 있다. 기본 문자같은 경우는 상관없겠지만 제어문자같은 특수한 문자는 통일되지 않는 경우가 있다.

따라서 명시적인 인코딩 방식을 지정하지 않고 수행하면 서버가 하나일 경우엔 상관이 없겠지만, 두 대 이상일 경우엔,

서버마다 같은 OS의 같은 버전이지 않는 한, 인코딩 방식이 달라져 값이 달라지므로, 검증을 할 수 없는 상황이 오게 된다.

따라서 OS마다 인코딩 시 다른 값이 나오지 않게 base64로 인코딩한 값을 넣는 것이다.

base64는 OS마다 다르게 인코딩될 수 있는 값들을 제외한 공통 ASCII만을 반환한다.

&nbsp;
## 가정

인코딩 과정(그냥 값을 넣을 경우)

!!  -> (송신측 운영체제의 인코딩)-> 010101 -> (네트워크) -> 010101 ->(수신측 운영체제의 디코딩)  →  ??

인코딩 과정(base64)

!! -> (송신측 운영체제의 인코딩) -> (base64) -> abc -> (송신측 운영체제의 인코딩)-> 010101 -> (네트워크) -> 010101 ->(수신측 운영체제의 디코딩) -> abc -> (base64)-> (수신측 운영체제의 디코딩) -> !! 

&nbsp;
# 📌 다양한 Token 종류

## OAuth

![image](https://github.com/inu-appcenter/server-study-16th/assets/62889359/6c192fc7-ce4b-469c-96a1-2cbfd7f59f50)

위처럼 애플리케이션을 이용할 때 구글, 카카오 등 제 3의 서비스에 저장된 계정 정보를 그대로 가져와 로그인 할 수 있는 기능은 OAuth라는 인증 표준을 통해 구현되었다.

OAuth란 Open Authorization의 약자이다. **애플리케이션이 특정 시스템의 보호된 리소스에 접근하기 위해, 사용자 인증(Authentication)을 통해 사용자의 리소스 접근 권한(Authorization)을 위임받는 것을 의미한다.**

&nbsp;
# 📌 Spring에서 JWT를 어떻게 생성할 수 있나요?

```java
implementation 'io.jsonwebtoken:jjwt:0.9.1'
```

```java
public String createAccessToken(Long memberId, String role) {
        //subject는 주제로 제목이라고 보면 된다.제목에 Id를 넣는다.
        Claims claims = Jwts.claims().setSubject(memberId);
        claims.put("role", role); // 정보는 key/value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장(클레임 저장)
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME)) // 토큰 유효 시간
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘
                .compact();
    }
```

&nbsp;
# 📌 JWT의 문제점

## 장점

- 토큰 검증만을 통해 사용자 정보를 확인가능 하여 추가 검증 로직이 필요 없다.
- 매번 세션이나 데이터베이스 같은 인증 저장소가 필요 없다.
- 사용자가 늘어나더라도 사용자 인증을 위한 추가 리소스 비용이 없다.

## 한계

- base64 인코딩 정보를 전달하기에 전달량이 많다.(base64는 원본 크기보다 33% 크다)
- 토큰이 탈취당할 시 만료될 때까지 대처가 불가능하다.
- Payload부분은 누구든 디코딩하여 확인할 수 있다.(중요한 정보를 담을 수 없다)
