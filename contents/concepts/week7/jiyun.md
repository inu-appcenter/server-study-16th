# ☀️ Refresh Token?

- Access Token의 유효기간이 만료되었을 때 서버에서 **새로운 Access Token을 발급**해주기 위해 사용하는 Token

- 유효기간이 **긴** Token


## ☔️ Refresh Token이 왜 탄생했을까요?

**JWT (Json Web Token)** 을 이용해 User를 인증하고 식별한다. JWT는 Stateless한 특징을 지니고 있다.<br>

이런 JWT의 특징으로 아래와 같은 문제점이 생긴다.

(1) 서버에서 토큰의 상태를 관리하지 않는다. (토큰과 관련된 내용은 관리하지 않음)

(2) 토큰을 탈취당했을 때 해당 토큰이 유효하게 하지 않게 되려면 유효기간이 만료될 때까지 기다려야 한다.

*문제점을 자세히 들여다보자면,*

(1) 서버는 해당 Token이 유효한지만 확인해 User를 인증 및 식별한다. 토큰을 사용하는 사람이 어떠한 사람인지를 확인하지 않는다. 때문에 Token을 탈취한 사람은 인증을 간단히 통과할 수 있게 되는 문제가 발생한다.

(2) 유효기간이 짧은 `Access Token` 만을 사용하는 웹사이트의 경우, 이용 중에 토큰이 만료돼서 로그아웃되거나 오류 메시지를 보게 된다.<br>
   그렇다고 `Access Token` 의 유효기간을 늘린다면, 외부 공격자에게 Token을 탈취 당했을 때 Token의 유효기간이 만료될 때까지 공격을 당해야 하는 보안상의 문제가 발생한다.<br>

즉, `Access Token` 하나만을 이용해 사용자를 인증하고 식별하기에는 **한계**가 있다. <br>

이러한 한계를 해결하기 위해 `Refresh Token` 이 탄생했다.

## ☔️ Refresh Token은 어떤 문제를 해결할 수 있을까요?

**Access Token**

- 유효기간 짧음
- 평소에 API 통신할 때 사용

**Refresh Token**

- 유효기간 긺
- Access Token이 만료돼 갱신될 때만 사용

<br>

외부인에게 탈취당할 위험이 큰 `Access Token` 의 만료 기간을 **짧게** 두고 `Refresh Token` 을 통해 주기적으로 `Access Token` 을 재발급함으로써 피해를 최소화하는 방식으로 쓰인다. <br>

- 탈취 위험성 감소

- Token 갱신의 유연
  - 사용자의 권한 변경, 정책 변경 등으로 Access Token의 권한을 변경해야 할 경우, Refresh Token을 사용해 새로운 Access Token 발급 가능 <br>=> 사용자는 새로 로그인하지 않고도 새로운 정책을 얻을 수 있게 됨

Refresh Token을 추가함으로써 Access Token을 하나만 이용했을 때의 문제 상황을 완화시키는 결과를 보여준다.

## ☔️ Refresh Token은 어떻게 구현할 수 있을까요?

![jwt](../Img/week7/JWT.png)

위의 사진은 인증 & 인가 과정을 보여준다.

1. 로그인시, 사용자가 인증된 사람이면 `Access Token & Refresh Token` 발급

2. 데이터 요청시 `Access Token` 과 함께 요청을 받음
3. 이때 `Access Token` 의 유효기간 == 만료, `Refresh Token` 을 이용해 `Access Token` 재발급
4. `Access Token` 과 `Refresh Token` 모두 유효시간 == 만료, Token 모두 재발급

<br>

```java

//Access Token & Refresh Token 발급 메소드
public String[] createTokenWhenLogin(int userIdx) {
        String AccessToken = createAccessToken(userIdx);
        String RefreshToken = createRefreshToken(userIdx);

        // 발급받은 RefreshToken 은 DB 에 저장
        jwtRepository.saveRefreshToken(RefreshToken, userIdx);
        String[] tokenList = {RefreshToken, AccessToken};
        return tokenList;
    }


-----------------------------------------
//Access Token 재발급 메소드
public String ReCreateAccessToken() throws BaseException {
        // Http Header 로부터 Refresh Token 추출
        String refreshToken = getRefreshToken();
        String dbRefreshToken;

        // refresh token 유효성 검증 (1) : DB조회
        try {
            RefreshToken dbRefreshTokenObj = jwtRepository.getRefreshToken(refreshToken);
            dbRefreshToken = dbRefreshTokenObj.getRefreshToken();
        } catch(Exception e){
            // DB에 RefreshToken 이 존재하지 않는경우
            throw new BaseException(BaseResponseStatus.NOT_DB_CONNECTED_TOKEN);
        }

        // DB에 Refresh Token이 존재하지 않거나(null), 전달받은 Refresh Token이 DB에 있는 Refresh Token과 일치하지 않는 경우 => exception
        if(dbRefreshToken == null || !dbRefreshToken.equals(refreshToken)){
            throw new BaseException(BaseResponseStatus.NOT_MATCHING_TOKEN);
        }

        // Refresh Token가 존재 & DB의 Refresh Token과 일치
        // refresh token 유효성 검증2 : 형태 유효성
        Jws<Claims> claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(Secret.REFRESH_TOKEN_SECRET_KEY)
                    .build()
                    .parseClaimsJws(refreshToken);
        }  catch (io.jsonwebtoken.security.SignatureException signatureException){
            throw new BaseException(BaseResponseStatus.INVALID_TOKEN);
        }
        catch (io.jsonwebtoken.ExpiredJwtException expiredJwtException) { // refresh token 이 만료된 경우

            // 새롭게 로그인을 시도하라는 Response 를 보낸다.
            throw new BaseException(BaseResponseStatus.REFRESH_TOKEN_EXPIRED);

        } catch (Exception ignored) { // Refresh Token이 유효하지 않은 경우 (만료여부 외의 예외처리)
            throw new BaseException(BaseResponseStatus.REFRESH_TOKEN_INVALID);
        }

        int userIdx = claims.getBody().get("userIdx", Integer.class);
        // Refresh Token을 이용한 검사 모두 통과 후, Access Token 재발급
        return createAccessToken(userIdx);
    }

```

<br>

## ☔️ Refresh Token은 어떤 문제가 있을까요?

- 보안 공격 위험성

  - Access Token의 탈취가 위험에서 완전히 벗어난 것은 아님
    - 탈취된 Access Token의 짧은 유효기간이라도 그 시간동안 악용될 위험성 존재
  - Refresh Token 자체 또한 탈취 가능

    - Refresh 탈취하면 해커는 마음껏 Access Token 만들 수 있음 (RTR를 이용해 어느정도 완화 가능)

  - Refresh Token이 탈취되면 공격자가 해당 토큰을 사용해 액세스 권한을 얻을 수 있음 => Refresh Token을 안전하게 저장하고 전송하는 것이 중요

- 유효기간 관리

  - 너무 길게 하면 보안 약화 시킴

  - 너무 짧게 하면 사용자의 경험 저하시킴 => 적절하게 관리해야 함

<br>

> :question: **RTR (Refresh Token Rotation)**
>
> - 클라이언트가 Access Token를 재요청할 때마다 Refresh Token도 새로 발급받는 것이다. 이렇게 되면 탈취자가 가지고 있는 Refresh Token은 더이상 만료 기간이 긴 토큰이 아니게 된다. 따라서 불법적인 사용의 위험은 줄어든다.
