# Spring Security란 무엇인가요?

### 1. 인증과 인가란 무엇일까요?

- **인증 (Authentication)**

 사용자가 사이트 접속을 위해서 입력한 사용자 정보가 실제 데이터 베이스의 정보와 일치한지 확인해서 사용자가 등록되어있는지 체크하여 등록된 사용자만 접근할 수 있도록 하는 접근 제어방식.

- **인가 (Authorization)**

 인증된 사용자가 특정 리소스 또는 기능에 접근할 권한에 대해서 체크를 하고 권한이 있다면 원하는 요청을 들어 정보를 얻도록 허용해주는 접근제어 방식 

 Spring Security는 사용자의 권한을 관리하고 보호된 리소스에 대한 접근을 허용하거나 거부하는데 사용된다. 

### 2. Spring Security의 구조 & 3. Spring Security의 작동 흐름

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/4275b07c-5390-489c-8bbf-82e4caa3dbbc/668d25c5-fcd9-4e60-8556-225468f7d3af/Untitled.png)

1. Http Request 수신

클라이언트로부터 로그인 정보와 함께 인증 요청을 하게 되면 `AuthenticationFilter` 에서 인증 및 권한 부여의 목적으로 일련의 필터를 거치게 된다. 

<aside>
💡 만약 로그인이 이미 되어 있다면?
→ 이미 Authentication객체가 Http Session에 저장 되어 있기 때문에 AuthenticationManager의 authenticate()메서드를 호출하지 않고 SecurityContextHolder에서 Authentication 객체를 가져와 인증 후 곧바로 클라이언트의 요청을 수행한다.

</aside>

- 유저 인증 관련 시큐리티에서 제공하는 필터
    - UsernamePasswordAuthenticationFilter : 사용자의 이름과 비밀번호를 사용한 기본 폼 기반 인증을 처리하는 필터. 사용자가 로그인을 하게 되면 이 필터가 사용자를 인증하고 세션에 사용자의 정보를 저장
    - DefaultLoginPageGeneratingFilter : 기본 로그인 페이지를 생성하고 제공하는 역할
    - DefaultLogoutPageGeneratingFilter: 기본 로그아웃 페이지를 생성하고 제공하는 역할
    - RememberMeAuthenticationFilter : 사용자가 이전에 로그인한 정보를 기반으로 자동 로그인을  지원하는 역할
    - RequestCacheAwareFilter : 인증되지 않은 사용자가 접근하려는 리소스에 대한 요청을 캐시하고, 사용자가 로그인한 후 이전 요청으로 돌아갈 수 있게끔 도와주는 필터
    - OAuth2LoginAuthenticationFilter : OAuth 2.0 을 통한 로그인을 처리하는 필터. 외부 로그인 서비스(카카오톡, 네이버, 구글 등)을 지원한다.
    - JwtAuthenticationFilter:  JWT(JSON Web Token)을 사용한 인증을 처리하는 필터. 클라이언트가 JWT 토큰을 제공하면 필터가 해당 토큰으로 사용자를 인증한다.
    - CasAuthenticationFilter : Central Authentication Service(CAS)를 통해 인증하는 필터로, 싱글 로그인(SSO, Single Sign-On)환경에서 사용된다.

1. 사용자 자격 증명을 기반으로 한 AuthenticationToken 생성 

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/4275b07c-5390-489c-8bbf-82e4caa3dbbc/00e36db5-acc5-43ae-85ff-3f76c3685691/Untitled.png)

유저의 인증 요청에서 AuthenticationFilter에 의해 유저의 이름과 패스워드를 추출한다. 

추출된 정보를 기반으로 인증 토큰(UsernamePasswordAuthenticationToken)이 생성된다. 생성된 토큰은 AuthenticationManager의 authenticate() 메서드를 통해 ProviderManager로 넘어가게 된다. 

1. AuthenticationManager 에게 인증 토큰 위임

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/4275b07c-5390-489c-8bbf-82e4caa3dbbc/1bd188c6-b6f0-4733-af85-a604187e098b/Untitled.png)

UsernamePasswordAuthenticationToken 개체 생성 후 AuthenticationManager의 인증 메서드를 호출하는데 사용된다. AuthenticationManager는 인터페이스이고 실제 구현은 ProviderManager에서 인증 토큰을 전달받아 사용된다. 

```java
public class ProviderManager implements AuthenticationManager {

    private List<AuthenticationProvider> providers = new ArrayList<>();

    public ProviderManager(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       // 리스트로 관리  
        for (AuthenticationProvider provider : providers) {
            if (provider.supports(authentication.getClass())) {
                try {
                    return provider.authenticate(authentication);
                } catch (AuthenticationException e) {
                    throw e;
                }
            }
        }
        throw new ProviderNotFoundException("No provider found for " + authentication.getClass().getName());
    }

    public void setProviders(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    public List<AuthenticationProvider> getProviders() {
        return providers;
    }
}
```

1. Authentication Provider 목록으로 사용자 인증 처리 
    
    특정 유형의 인증을 처리하도록 구성 
    
- DaoAuthenticationProvider : 유저 이름과 비밀번호를 사용한 인증 처리
- JwtAuthenticationProvider : JWT를 사용한 인증 처리

supports 메서드 : 주어진 Authentication 객체 타입을 AuthenticationProvider가 지원하는지 여부 판단 

지원하면 → 인증 수행 → 완전히 인증된 Authentication 객체를 반환 

1. UserDetailsService에 Authentication 객체 넘기기

UserDetailsService는 유저의 정보를 가져오는 인터페이스로 해당 인터페이스를 구현하기 위해서는 DB로부터 유저의 정보를 불러와 UserDetails 로 반환하는 loadUserByUsername 메서드를 구현해야 한다. 

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new CustomUserDetails(user.getUsername(), user.getPassword(), user.isEnabled(), getAuthorities(user));
    }
   ...
}
```

1. UserDetailsService로부터 UserDetails 검색 

UserDetails 인터페이스는 사용자 정보를 캡슐화하고 사용자의 인증 및 권한 정보를 Spring Security와 함께 사용할 수 있도록 설계되었다. DB에서 사용자의 정보를 토대로 검색하고, 해당 데이터를 UserDetails 객체에 매핑시켜 반환한다. 

- UserDetails의 주요 메서드
    
    
    |                      메서드 | 설명  |
    | --- | --- |
    | getAuthorities() | 사용자에게 부여된 권한 목록을 반환  
    ( 반환 타입 : Collection<? extends GrantedAuthority>  |
    | getPassword() | 사용자의 암호화된 비밀번호를 반환 |
    | getUsername() | 사용자의 이름 반환 |
    | isAccountNonExpired() | 사용자의 계정이 만료되었는지 여부 |
    | isAccountNonLocked() | 사용자의 계정이 잠겨있는지 여부 |
    | isCredentialsNonExpired() | 사용자의 자격증명(비밀번호)이 만료되었는지 여부 |
    | isEnabled() | 사용자의 계정이 활성화 되었는지 여부  |
1. AuthenticationProviders 에게 UserDetails 객체 전달 
    
    AuthenticationProviders는 UserDetails를 받아 유저 정보를 비교한다. 
    

1. 성공 시 인증객체 반환 OR 실패시 인증 예외 발생 

인증 완료 시 Principal(주체), Credentials(자격 증명), Authorities(권한), Authenticated(인증 여부)를 설정한 Authentication 객체를 반환하고, 인증에 실패하면 상황에 맞는 예외를 발생시킨다. 

- **Authentication 객체**
    - Principal : 주체
        - 사용자의 아이디 혹은 User 객체
        - getPrincipal() 메서드를 통해 사용자를 나타내는 주체를 얻을 수 있다.
    - Credentials : 자격 증명
        - 사용자 비밀번호
        - getCredentials() 메서드를 통해 사용자의 자격증명( 비밀번호, 토큰)을 얻을 수 있다.
        - 사용자를 인증하기 위한 정보
    - Authorities : 권한
        - 인증된 사용자의 권한 목록
        - getAuthorities() 메서드를 통해 사용자에게 부여된 권한 목록을 얻을 수 있다.
    - Authenticated : 인증 여부
        - getAuthenticated() 메서드는 사용자가 인증되었는지 여부를 나타내며,
        - 인증되면 true, 그렇지 못하면 false를 반환

✔️ 인증되지 않은 상태

```java

UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("username", "password");

```

✅ 인증된 상태

```java
List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));

User user = new User("username", "password", authorities);

UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
```

1. 인증 완료

AuthenticationManager는 Authentication 객체를 관련 AuthenticationFilter 로 반환한다.

1. SecurityContext 에 Authentication 객체 저장

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/4275b07c-5390-489c-8bbf-82e4caa3dbbc/8630f966-91d5-47fb-bde5-a14d42e8120d/Untitled.png)

- SecurityContextHolder
    - 세션 영역에 있는 SecurityContext에 Authentication객체를 저장
- SecurityContext
    - Authentication 객체가 직접 저장되는 저장소
    - 필요시 객체를 가져올 수 있다.

정보 접근 코드 

```java
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
String username = authentication.getName();
```

### 4. Spring Security에서 할 수 있는 보안 설정

- CSRF 방지
    - 악의적인 사이트가 사용자의 의도와 상관없이 사용자의 자격증명을 사용하여 다른 사이트에 요청을 보내는 것을 방지

```java
// 보호 활성화
.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // CSRF 토큰을 쿠키로 저장

// 보호 비활성화
// CSRF 토큰이 없어도 요청 처리 가능 
.csrf((csrfConfig) -> csrfConfig.disable())
```

- CORS( Cross-Origin Resource Sharing)
    - 다른 도메인의 리소스에 웹 페이지가 접근할 수 있도록 브라우저에게 권한을 부여하는 메커니즘
- URL 기반 접근 제어
    - 특정 URL 에 대한 접근 권한 설정
- **Form-based Authentication**
    - 사용자 이름과 비밀번호를 사용하는 기본 로그인 폼 제공.
- **Role 및 Authority 기반 인가**
    - 사용자에게 역할(Role)과 권한(Authority)을 부여하여 접근 제어.
- **세션 관리**:
    - **세션 고정 보호(Session Fixation Protection)**: 세션 고정 공격 방지.
    - **동시 세션 제어(Concurrent Session Control)**: 사용자당 허용된 동시 세션 수 제한.
    - **세션 타임아웃(Session Timeout)**: 세션의 유효 기간 설정.
- **비밀번호 정책**:
    - 비밀번호 복잡성 규칙 설정, 비밀번호 만료 정책 등.
- **Security Headers**:
    - 추가적인 보안 헤더 설정(예: **`X-Content-Type-Options`**, **`X-Frame-Options`**, **`X-XSS-Protection`** 등).
- **Remember Me 기능**:
    - 로그인 상태 유지(remember-me) 기능 설정.
- **로그인/로그아웃 설정**:
    - 로그인 및 로그아웃 URL, 성공/실패 핸들러 설정

### 5. SecurityConfiguration 클래스 설명

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 토큰 없이 요청 처리
                .csrf((csrfConfig) ->
                        csrfConfig.disable())
                .cors((corsConfig) ->
                        corsConfig.disable())
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/","/swagger-ui/**","/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/members","api/members/login").permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
    }

}

```
