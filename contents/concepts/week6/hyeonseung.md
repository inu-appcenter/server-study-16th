# 자바에서의 테스트 코드 작성

## 자바에서 테스트를 위해 사용되는 메소드

JUnit 프레임워크 : 테스트 프레임 워크로 주로, 단위 테스트를 작성하고 실행할 수 있는 기능이 있음

- JUnit 특징
    - @Test 메서드가 호출할 때마다 새로운 인스턴스를 생성하여 독립적인 테스트가 이루어짐.
    - 단정(assert) 메서드로 테스트 케이스의 수행결과를 판별 가능

📌 기본 어노테이션 

- @Test : 테스트를 진행할 메소드를 표시
- @BeforeEach : 각 테스트 메소드가 실행되기 전에, 실행되는 메소드
- @AfterEach : 각 테스트 메소드가 실행된 후, 실행되는 메소드
- @BeforeAll : 모든 테스트 메소드가 실행되기 전에, 한 번 실행되는 메소드
- @AfterAll : 모든 테스트 메소드가 실행된 뒤에, 한번 실행되는 메소드
- @Disabled : 특정 테스트 메소드를 비활성화함

| assertArrayEquals(expected, actual) | 예상값 expected와 실제값 actual이 일치함을 확인 |
| --- | --- |
| assertEquals(expected, actual)   | 예상 값과 실제값이 같은 값을 가지는 지 확인 |
| assertEquals( a, b, c) |  c의 오차범위 내에서 객체 a 와 b가 같이 일치하는 지 확인. |
| assertSame(a, b)  |  객체 a 와 b가 같은 객체임을 확인 |
| assertTrue(condition) |  조건이 참인지 확인 |
| assertNotNull(actual)   |  값이 null이 아님을 확인  |
| assertThrows(expectedType, executable) | 특정 예외가 발생하는지 확인  |
| assertTimeout(duration, executable) | 특정 시간 안에 실행이 완료되는지 확 |

AssertJ 라이브러리

기본 JUnit이 제공하는 메소드보다 더 다양한 Assertion 기능과 가독성 높은 문법을 제공. 

⇒ 컬렉션이나 문제열 같은 복잡한 객체의 상태를 검증하는데 유용

기본 어셜션

| isEqualTo() , isNotEqualTo() | 값이 같은지 , 같지 않은지 확인 |
| --- | --- |
| isNull(), isNotNull() | 값이 Null인지 , 아닌지 확인 |
| isTrue() , isFalse() | 값이 true() 인지, 아닌지  확인 |

문자열 어셜션

| startsWith(), endsWith | 특정 문자열로 시작하는지, 끝나는지 확 |
| --- | --- |
| contains(), doesNotContain() | 특정 문자열을 포함하는지, 포함하지 않는지 확인 |
| isEmpty(), isNotEmpty() | 문자열이 비어있는지 비어있지 않은지 확인 |

컬렉션 어셜선

| hasSize() | 컬렉션의 크기를 확인  |
| --- | --- |
| contains() | 컬렉션이 특정 요소를 포함하는지 확인 |
| containExactly() | 컬렉션이 정확히 특정 요소들만 포함하는지 확인 |
| containsAnyOf() | 컬렉션이 특정 요소들 중 하나라도 포함하는지 확인 |
| doesNotContain() | 컬렉션이 특정 요소를 포함하지 않은지 확인 |
| isEmpty(), isNotEmpty(( | 컬렉션이 비어있는지 , 비어 있지 않은지 확인 |

예외 어셜션

| assertThatThrownBy()  | 특정 코드 블록이 예외를 던지는지 확인 |
| --- | --- |
| isInstanceOf() | 예외가 특정 클래스의 인스턴스인지 확인  |
| hasMessage() | 예외의 메시지가 특정 문자열인지 확인 |
| hasMessageContaining() | 예외의 메시지가 특정 문자열을 포함하는지 확인 |
- Mockito

실제 객체를 모방한 가짜 객체, 즉 Mock 객체 생성 

`@Mock` 이나 `@MockBean` 애노테이션 사용하여 주입하면 됨

## 실제  자바에서의 테스트 코드 예시

<aside>
💡 given - when - then 구조를 활용

given : 테스트 대상 코드가 어떤 입력값을 받아들이는지, 입력값이 어떤 상황을 나타내는지 표현해주기 
when : 실제 테스트 대상 코드 실행
then : 예상되는 결과를 검증

</aside>

### Service 계층에서의 테스트 코드 예시

```java

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    private Member member;
    
    private MemberRequestDto requestDto;
    
    @Mock
    private MemberRepository memberRepository;
    
    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .name("test")
                .loginId("test123")
                .password("test123!@")
                .build();

        memberService = new MemberService(memberRepository);
        requestDto = new MemberRequestDto("test", "test123", "test123!@");

    }

    @Test
    @DisplayName("멤버 생성")
    void createMemberSuccess(){

        member = MemberMapper.INSTANCE.toEntity(requestDto);

        when(memberRepository.existsByLoginId(requestDto.getLoginId())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberResponseDto responseDto = memberService.create(requestDto);

        assertThat(responseDto.getLoginId()).isEqualTo(requestDto.getLoginId());
        assertThat(responseDto.getName()).isEqualTo(requestDto.getName());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("로그인")
    void LoginMember() {
        when(memberRepository.findByLoginId(requestDto.getLoginId())).thenReturn(Optional.of(member));

        MemberRequestDto.LoginRequestDto loginRequestDto = new MemberRequestDto.LoginRequestDto("test123" ,"test123!@");
        MemberLoginResponseDto responseDto = memberService.login(loginRequestDto);

        assertThat(responseDto.getLoginId()).isEqualTo(requestDto.getLoginId());
    }

    @Test
    @DisplayName("단일 회원 검색")
    void SearchMemberById(){
        setMemberId(member, 1L);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        MemberResponseDto responseDto =  memberService.searchId(1L);

        assertThat(responseDto.getId()).isEqualTo(member.getId());
        assertThat(responseDto.getLoginId()).isEqualTo(requestDto.getLoginId());
        assertThat(responseDto.getName()).isEqualTo(requestDto.getName());
    }

    @Test
    @DisplayName("전체 회원 검색")
    void SearchAll(){
        when(memberRepository.findAll()).thenReturn(Arrays.asList(member));

        List<MemberResponseDto> responseDtos = memberService.searchAll();

        assertThat(responseDtos).hasSize(1);
        assertThat(responseDtos.get(0).getLoginId()).isEqualTo(requestDto.getLoginId());
    }

    @Test
    @DisplayName("회원정보 수정")
    void updateMember() {
        setMemberId(member,1L);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(memberRepository.existsByLoginId(requestDto.getLoginId())).thenReturn(false);

        MemberRequestDto newRequestDto = new MemberRequestDto("newTest", "newTest123", "test456!@#");

        MemberResponseDto responseDto = memberService.update(1L, newRequestDto);

        assertThat(responseDto.getId()).isEqualTo(member.getId());
        assertThat(responseDto.getLoginId()).isEqualTo(newRequestDto.getLoginId());
        assertThat(responseDto.getName()).isEqualTo(newRequestDto.getName());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원삭제")
    void deleteMember(){
        setMemberId(member, 1L);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        memberService.delete(1L);

        verify(memberRepository, times(1)).deleteById(1L);

    }

    private void setMemberId(Member member, Long id) {
        try {
            Field idField = Member.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(member, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}

```
