# 2주차_ORM은 무엇일까요? / 스프링에서 ORM을 어떻게 이용할 수 있을까요?

생성일: 2024년 4월 7일 오후 5:13
태그: 서버 스터디

# ORM 등장 배경

### Persistence Layer

![image](https://github.com/inu-appcenter/server-study-16th/assets/86196038/9de936ab-d06b-4f61-8ae9-ab615e31f6d4)

> Persistence (영속성)은 데이터를 생성한 프로그램이 종료되더라도 사라지지 않는 데이터의 특성을 의미한다.
> 

우리가 개발을 할 때 객체의 상태를 DB에 저장하게 되는데 이를 객체에 영속성을 부여해주었다고 한다. 

그리고 Mark Richards의 소프트웨어 아키텍처 패턴에서 객체에 영속성을 부여해주는 계층을 Persistence Layer라고 한다.

이 Persistence Layer를 구현하는 방법은 크게 JDBC만을 이용하거나 Persistence Framework를 이용하는 방법이 있다.

### JDBC만을 이용한 영속 계층

> JDBC(Java Database Connectivity)는 자바에서 데이터베이스에 접속할 수 있도록 도움을 주는 자바 API다.
> 

JDBC를 활용하면 DBMS가 제공하는 JDBC 드라이버를 이용해 DBMS의 종류와 상관없이 하나의 JDBC API를 이용하여 DB 작업을 처리할 수 있다.

![image 1](https://github.com/inu-appcenter/server-study-16th/assets/86196038/0ea9777b-8475-4dc0-9918-eafcdc30d28d)

- 하지만 JDBC만을 이용하여 개발할 경우에는 불편한 점이 꽤 많다.
    - 간단한 SQL을 실행함에도 중복된 코드를 반복적으로 사용
    - DB에 따라 일관성 없는 정보를 가진 채로 Checked Exception(SQLException) 처리
    - 연결과 같은 공유 자원을 제대로 반환하지 않으면 시스템의 자원 부족 현상 발생

때문에 복잡하고 번거로운 작업 해결을 위해 데이터베이스와 연동되는 시스템을 빠르게 개발할 수 있는 Persistence Framework를 사용하기 시작했다.

### SQL Mapper를 이용한 영속 계층

> SQL Mapper는 SQL문과 객체를 매핑하여 데이터를 객체화하는 Persistence Framework다. 객체 간의 관계를 매핑하는 것이 아닌 직접 작성한 SQL문의 결과와 객체의 필드를 매핑하는 것이 메인 컨셉이다.
> 

SQL Mapper의 종류로는 JDBC Template와 MyBatis가 있다.

- SQL Mapper를 이용하면 기존 JDBC만을 이용했을 때보다 간결한 코드와 향상된 유지보수성을 얻을 수 있었지만, 이 또한 몇 가지 문제를 갖고 있었다.
    - SQL을 직접 작성하기에 특정 DB에 종속적
    - 비슷한 CRUD SQL 작성 및 DAO를 반복적으로 개발
    - 테이블 필드 변경 시 유지 보수하기 힘듦

코드 상에서는 SQL과 JDBC API를 분리했지만 논리적으로는 강한 의존성을 갖고 있어 SQL에 의존적인 개발을 해야 하는 문제가 발생했다.

또한 RDB에서는 데이터 중심의 구조를 지니고 있기에 객체 중심 구조인 객체 지향과 패러다임의 불일치가 생겼다. 

# ORM을 이용한 영속 계층

![image 2](https://github.com/inu-appcenter/server-study-16th/assets/86196038/e826e7a0-85c3-4e30-9661-e13ff2527710)

> ORM(Object Relational Mapping) 은 객체와 RDB의 테이블을 매핑하는 것을 의미한다.
> 

ORM은 SQL Mapper의 패러다임 불일치를 해소하기 위해 나타났다.

ORM은 객체 간의 관계들을 바탕으로 SQL문을 자동으로 생성하고 메서드를 통해 직관적으로 데이터를 조작하게 하여 개발자의 불편함을 해소한다.

### ORM은 왜 필요한가?

ORM을 사용하면 다음과 같은 장점을 얻을 수 있다.

1. **추상화:** ORM은 높은 수준의 추상화를 제공하여 데이터베이스와의 상호 작용을 단순화한다. 이를 통해 개발자는 SQL 쿼리 대신 객체로 작업할 수 있다.
2. **보일러 플레이트 코드 감소:** ORM은 대부분의 일반적인 데이터베이스 작업을 ORM 시스템이 처리하기 때문에 반복적인 보일러 플레이트 코드를 줄일 수 있다.
    
    > **보일러 플레이트 코드**는 최소한의 변경으로 여러 곳에서 재사용되며, 반복적으로 비슷한 형태를 띄는 코드를 의미
    > 
3. **이식성:** ORM 시스템은 일반적으로 여러 데이터베이스 시스템을 지원하므로 개발자는 코드 변경을 최소화하면서 서로 다른 데이터베이스 간에 전환을 쉽게 할 수 있다.
4. **생산성:** ORM을 사용하면 개발자는 복잡한 SQL 쿼리를 작성하는 대신 애플리케이션의 비즈니스 논리에 집중할 수 있다. 이는 생산성 향상으로 이어질 수 있다.
5. **유지보수성:** ORM은 객체 지향 설계 패턴을 사용해 구조화되고 유지, 관리하기 쉬운 코드를 만든다.

### ORM의 단점

- query가 복잡해지면 ORM으로 표현하는데 한계가 있다.
- 성능이 raw query에 비해 느리다.

ORM의 단점을 극복하기 위해 JPQL, QueryDSL 등을 사용하거나 Mybatis를 함께 사용하기도 한다.

# JPA

JPA는 ORM의 대표 기술이다.

> **JPA (Java Persistence API)**는 Java 진영에서 ORM(Object-Relational Mapping) 기술 표준으로 사용하는 인터페이스 모음
>

<img width="80%" alt="image 3" src="https://github.com/inu-appcenter/server-study-16th/assets/86196038/03f36a5a-822a-4989-a1ae-3d31ca00ad4b">

JPA는 영속성 컨텍스트인 EntityManager를 통해 Entity를 관리한다.

Entity는 DB와 매핑 되어 사용자가 Entity에 대한 CRUD를 실행을 했을 때 Entity와 관련된 테이블에 대한 적절한 SQL 쿼리문을 생성하고, 이를 관리하였다가 필요 시 JDBC API를 통해 DB에 날리게 된다.

### JPA 특징

- 1차 캐시와 동일성 보장
    
    JPA에서는 같은 트랜잭션 안에서는 같은 엔티티를 반환한다. 
    
    조회 쿼리를 보내고 결과를 받아올 때, 받아온 결과를 캐시 해두고 사용하여 동일한 조회 코드를 두 번 작성해도 단 하나의 쿼리만 발생한다.
    
- 쓰기 지연 기능
    
    쓰기 지연 기능은 트랜잭션을 커밋할 때까지 INSERT 쿼리문을 모아두는 기능을 의미한다. 
    
    쓰기 지연 기능을 이용하면 쿼리문을 모아두었다가 한 번에 보내기 때문에 네트워크 통신 비용이 절감되는 장점이 있다.
    
- 지연 로딩 & 즉시 로딩
    
    JPA에서는 지연 로딩과 즉시 로딩 기능을 제공해준다. 
    
    지연 로딩은 객체가 사용될 때 로딩하는 것을 의미하고, 즉시 로딩은 JOIN SQL로 한번에 연관된 객체까지 조회하는 것을 의미한다.
    

### Spring Data JPA

> **Spring Data JPA**는 JPA 기반 애플리케이션 개발을 보다 간편하게 만드는 라이브러리/프레임워크이다.
>

![img1 daumcdn](https://github.com/inu-appcenter/server-study-16th/assets/86196038/498b7710-7ae8-45e3-aec3-c95640619be8)

<img width="50%" src="https://github.com/inu-appcenter/server-study-16th/assets/86196038/8d0ac29f-4390-45f8-888e-201ae873b2ba">

JPA는 단순한 명세이기 때문에 JPA만 가지고는 어떤 구현 기술을 사용할 수 없다.

실제로 우리가 사용하는 Repository는 **Spring Data JPA**로부터 제공되는 기술이다.

JPA의 구현체로는 Hibernate, EclipseLink, DataNucleus 등 다양하게 존재한다. 

Hibernate는 이들 중 가장 범용적으로 다양한 기능을 제공하기 때문에 주로 사용된다.

### Repository Layer란?

Repository Layer는 Layered Architecture의 Controller + Service + Repository + Dao 4계층 중 Repository에 해당하는 계층이다. 

> Layered Architecture (계층화 아키텍처)는 각 구성 요소들이 ‘관심사의 분리’를 달성하기 위해 ‘책임’을 가진 계층으로 분리한 아키텍처이다.
> 
> 
> <img width="70%" src="https://github.com/inu-appcenter/server-study-16th/assets/86196038/058941b2-07ff-4b12-8325-e2cd99fd7d55">
> 

계층 구조는 처음에는 Controller + Dao의 2계층으로부터 시작되었다.

Dao는 DB 관련 책임 (CRUD 수행)을 가지고 있었고 Controller는 DB 관련 이외의 책임을 가지고 있었다. 

하지만 Controller가 너무 많은 책임을 지고 있어 이를 분리하고자 Service 계층이 나타났다.

Controller + Service + Dao의 3계층은 Controller로부터 비즈니스 로직 수행을 책임지는 Service를 분리시켰다.

다만 이 경우에도 Service는 비즈니스 로직 수행 책임과 도메인 ↔ DB 전달 객체(DTO, Entity)로 변환 책임 2가지를 지니고 있었다.

이로써 Controller + Service + Repisitory + Dao의 4계층이 생겨났다.

Service는 비즈니스 로직을 수행하는 책임만 갖게 되었고 DB에 전달하는 객체의 관리 책임은 Repository가 갖게 되었다.

> **Controller + Service + Repisitory + Dao의 4계층**
> 
> - **Controller** : DB 관련 이외의 책임
>     - HTTP 요청 & 응답 변환 책임 (직렬화/역직렬화 : 데이터 포맷 <-> 자바 객체 변환)
> - **Service** : 비즈니스 로직 수행 책임
> - **Repository** : 도메인 <-> DB 전달 객체(DTO, Entity)로 변환하여 Service와 상호작용 책임
> - **Dao** : DB CRUD 수행 책임

### JpaRepository 인터페이스

Spring Data JPA는 간단한 CRUD 기능을 공통으로 처리하는 인터페이스를 제공한다.

이를 **JPA 공통 인터페이스**라고 한다.

```java
public interface UserRepository extends JpaRepository<User, Long>{
}
```

Jpa 공통 인터페이스는 간단하지만 단순 반복으로 작성해야 하는 CRUD 작업들을 Spring Data JPA 구현체인 Hibernate가 애플리케이션 실행 시점에 동적으로 자주 사용되는 쿼리 집합을 만들어 위처럼 UserRepository 인터페이스를 구현해준다.

이로써 자주 사용하는 CRUD를 굳이 JPQL로 작성하지 않더라도 인터페이스 하나만 상속 받으면 사용할 수 있게 된다.

JpaRepository 인터페이스는 메서드 이름만으로 쿼리를 생성할 수 있다.

**Method**

| method | 기능 |
| --- | --- |
| save() | 레코드 저장 (insert, update) |
| saveAll() | Iterable 가능한 객체를 저장 |
| findOne() | primary key로 레코드 한건 찾기 |
| findAll() | 전체 레코드 불러오기. 정렬(sort), 페이징(pageable) 가능 |
| count() | 레코드 갯수 |
| delete() | 레코드 삭제 |

**Keyword**

| 메서드 이름 키워드 | 샘플 | 설명 |
| --- | --- | --- |
| And | findByEmailAndUserId(String email, String userId) | 여러필드를 and 로 검색 |
| Or | findByEmailOrUserId(String email, String userId) | 여러필드를 or 로 검색 |
| Between | findByCreatedAtBetween(Date fromDate, Date toDate) | 필드의 두 값 사이에 있는 항목 검색 |
| LessThan | findByAgeGraterThanEqual(int age) | 작은 항목 검색 |
| GreaterThanEqual | findByAgeGraterThanEqual(int age) | 크거나 같은 항목 검색 |
| Like | findByNameLike(String name) | like 검색 |
| IsNull | findByJobIsNull() | null 인 항목 검색 |
| In | findByJob(String … jobs) | 여러 값중에 하나인 항목 검색 |
| OrderBy | findByEmailOrderByNameAsc(String email) | 검색 결과를 정렬하여 전달 |

위의 메서드와 키워드를 조합하여 메서드 이름을 생성하면 대부분의 쿼리문을 작성할 수 있다.

![images_hoyun7443_post_5f456605-e33b-4e04-864d-16465b3156d1_image](https://github.com/inu-appcenter/server-study-16th/assets/86196038/763f0aaa-c318-4487-9248-076fb17adcc5)

또한 위 사진은 JpaRepository 인터페이스의 계층 구조를 보여주는데 

- 최상위에 있는 Repository 인터페이스는 특별한 기능을 제공하는 것이 아닌 마크 인터페이스로, 해당 인터페이스가 Repository 용도로 사용될 것임을 알리는 데 쓰인다.
- Repository 인터페이스를 상속받는 CrudRepository는 비로소 CRUD 기능을 사용할 수 있도록 해주고,
- PagingAndSortingRepository 인터페이스는 단순 CRUD 외에 Paging, Sorting과 같은 기능들도 수행할 수 있게 해준다.
- NoRepositoryBean 인터페이스는 @NoRepositoryBean 어노테이션을 붙여 해당 인터페이스가 Repository 용도로서 사용되는 것이 아닌 단순히 Repository의 메서드를 정의하는 인터페이스라는 정보를 부여할 수 있게 한다.
    
    ```java
    @NoRepositoryBean
    public interface MyRepository<T, ID extends Serializable> extends Repository<T, ID> {
    
        <E extends T> E save(E entity);
    
        List<T> findAll();
    
        long count();
    }
    
    public interface CommentRepository extends MyRepository<Comment, Long>{
    
        Comment save(Comment comment);
    
        List<Comment> findAll();
    }
    ```
    
- QueryByExampleExecutor 인터페이스는 조회 메서드에 Example 인터페이스를 인자로 받을 수 있게 지원한다.
    
    Example은 조건을 부여하는 기능을 하는데, 엔티티 자체를 조건으로 한다.
    
    ```java
    public interface QueryByExampleExecutor<T> {
     
    	<S extends T> Optional<S> findOne(Example<S> example);
     
    	<S extends T> Iterable<S> findAll(Example<S> example);
        
        ...
    }
    ```
    
    ```java
    // 검색어를 포함하는 검색 대상 엔티티인 probe 생성
    Team team = new Team();
    team.setName("t");
    
    Member member = new Member();  // member가 probe 이다
    member.setName("m");
    member.setTeam(team);
    
    // 검색 조건을 표현하는 ExampleMatcher
    ExampleMatcher matcher = ExampleMatcher.matchingAny()  // 모든 matcher를 or 로 연결
      .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase()) // name에 대소문자 불문 m 포함
      .withMatcher("team.name", ExampleMatcher.GenericPropertyMatchers.contains()) // team.name에 대소문자 구분 t 포함
      .withIgnorePaths("gender", "team.description") // gender. team.description 검색 조건에서 제외
      .withIgnoreNullValues(); // 값이 null인 필드 검색 조건에서 제외
    
    // 검색 조건을 담고 있는 Example
    Example<Member> example = Example.of(member, matcher);
    ```
    

# 연관관계 매핑이란?

> 연관관계 매핑이란 객체의 참조와 테이블의 외래 키를 매핑하는 것을 말한다.
> 

### 연관관계 매핑이 필요한 이유

연관관계의 매핑이 필요한 이유는 객체 지향적으로 코드를 구현하기 위해서이다.

DB의 테이블에서는 연관 관계를 구사하기 위해 외래키를 사용한다. 

테이블과 동일하게 객체에 외래키를 넣어준다면 객체와 연관되어있는 객체를 찾기 위하여 그 키 값으로 객체를 또 찾아주어야 하는 번거로움이 있다. 

하지만 객체적인 구현이라면 다른 객체를 참조하는 값을 필드로 갖고 있으면 된다.

이렇게 구현을 하기 위해 매핑을 해주는 것이다.

### 연관관계 매핑 시 고려사항

1. 방향(Direction)
    
    방향에는 단방향과 양방향이 있다.
    
    객체는 참조용 필드를 가지고 있는 객체만 연관된 객체를 조회할 수 있으므로 방향이 존재한다. 두 객체가 서로 참조하는 관계를 양방향 관계, 한 객체에서 다른 객체만 참조하는 관계를 단방향 관계라 한다.
    
    ```java
    class A {
        B b;
    }
    
    class B {
        A a;
    }
    ```
    
    테이블은 외래 키 하나로 양쪽으로 조인이 가능하다. 따라서 테이블은 방향이 없다고 볼 수도 있고, 항상 양방향이라 할 수 있다.
    
    ```sql
    SELECT *FROM MEMBER M
    JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID
    
    SELECT *FROM TEAM T
    JOIN MEMBER M ON T.TEAM_ID = M.TEAM_ID
    ```
    
2. 다중성(Multiplicity)
    - 다대일(N:1, ManyToOne)
    - 일대다(1:N, OneToMany)
    - 일대일(1:1, OneToMany)
    - 다대다(N:M, ManyToMany)
3. 연관관계의 주인(Owner)
    
    연관관계를 관리 포인트는 외래 키인데, 양방향 관계를 맺으면 객체 서로가 외래 키를 가질 수 있게 된다. 따라서 **두 객체 중 하나를 외래 키를 관리**해야 한다. 외래 키를 관리하는 객체를 **연관관계의 주인이라 한다.**
    
    테이블과 객체를 설계할 때 **외래 키를 가지는 엔티티를 연관관계 주인**으로 정하는데, 그 이유는 외래 키를 가진 테이블과 매핑되는 엔티티가 외래 키를 관리하는 것이 효율적이기 때문이다.
    
    **연관관계 주인만이 데이터를 등록, 변경할 수 있으며, 주인이 아닌 객체는 읽기만 가능하다.**
    

### 연관관계 매핑을 어떻게 이용할까?

- **N:1 관계**
    
    **N:1 단방향 매핑**
    
    가장 많이 사용하는 연관관계이다.
    
    N:1 자체로도 많이 사용하지만, M:N 관계를 매핑할 때 사용하기도 한다.
    
    ```java
    public class Member {
        @Id @GeneratedValue
        private Long id;
        private String name;
    
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "team_id")
        private Team team;
    }
    ```
    
    - **@ManyToOne**: 말 그대로 **N:1**을 나타내는 어노테이션이다.
    - **@JoinColumn**: Team을 참조할 **Foreign Key**의 이름을 정해준다.
    
    ---
    
    **N:1 양방향 매핑**
    
    아래와 같이 Team에서 Member 리스트를 갖고 있을 수 있다.
    
    Member에서 Team을 **@ManyToOne**으로 매핑해야 사용할 수 있다.
    
    이 경우, Team은 Member 리스트를 읽는 것만 허용되고, 등록이나 수정은 할 수 없다. (cascade 속성을 설정해주면 가능하다.)
    
    ```java
    public class Team {
        @Id @GeneratedValue
        private Long id;
        private String name;
    
        @OneToMany(mappedBy = "team")
        private List<Member> member;
    }
    ```
    
    - **@OneToMany**: **1:N** 관계를 나타내는 어노테이션
    - **mappedBy**: Member 테이블의 Team 변수명. private Team **team**; 이라고 선언했기 때문에 **"team"**이 된다.
    
    ---
    
- **1:N 관계**
    
    **1:N 단방향 매핑**
    
    한 객체가 반대편 객체를 List 형태로 갖는 구조이다.
    
    **1:N** 특성 상 외래 키가 **N** 쪽에 있어야 하기 때문에,
    
    연관관계의 주인이 반대편 테이블의 **Foreign Key**를 관리하는 특이한 구조가 된다.
    
    ```java
    public class Team {
        @Id @GeneratedValue
        private Long id;
        private String name;
    
        @OneToMany(fetch = FetchType.LAZY)
        @JoinColumn(name = "team_id")
        private List<Member> member;
    }
    ```
    
    - **@OneToMany**: 1:N 매핑
        - **FetchType의 LAZY와 EAGER**
            - LAZY
                - 지연로딩
                - 연관관계가 설정된 테이블에 대해 select를 하지 않는다.
                - 1:N 과 같이 여러가지 데이터가 로딩이 일어날 경우 사용하는 방식
            - EAGER
                - 즉시로딩
                - 연관관계가 설정된 모든 테이블에 대해 조인이 이루어진다.
                - 1:1 연관관계와 같이 한 건만 존재할 때 사용하는 방식
    - **@JoinColumn**: Member 테이블에 생성될 **Foreign Key**의 이름을 명시한다. 만약 **@JoinColumn**이 없을 경우 중간에 테이블이 하나 추가되기 때문에 꼭 사용해야 한다.
    
    이 관계는 구조도 비정상적이고, 연관관계 관리를 위해 UPDATE 쿼리까지 날려야 해서 비효율적이다.
    
    그러니 꼭 사용해야 하는게 아니라면 N:1 양방향 매핑을 사용하는 것이 좋다.
    
    ---
    
    **1:N 양방향 매핑**
    
    이런 매핑은 없다.
    
    읽기 전용 필드를 사용하는 꼼수가 있지만, 그럴바엔 **N:1** 양방향을 사용하자.
    
    ```java
    public class Member {
        @Id @GeneratedValue
        private Long id;
        private String name;
    
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "team_id", insertable = false, updatable = false)
        private Team team;
    }
    ```
    
    ---
    
- **1:1 관계**
    
    **1:1 단방향 매핑**
    
    **1:1**은 뒤집어도 **1:1**이기 때문에 **Foreign Key**를 어디에 두어도 상관없다. 상황에 맞게 선택하자.
    
    매핑 방식은 **N:1**과 유사하다. **@ManyToOne / @OneToMany**를 **@OneToOne**으로 바꿔주기만 하면 된다.
    
    ```java
    public class Member {
        @Id @GeneratedValue
        private Long id;
        private String name;
    
        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "team_id")
        private Team team;
    }
    ```
    
    ---
    
    **1:1 양방향 매핑**
    
    ```java
    public class Team {
        @Id @GeneratedValue
        private Long id;
        private String name;
    
        @OneToOne(mappedBy = "team")
        private Member member;
    }
    ```
    
    ---
    
- **M:N 관계**
    
    **N:1**과 마찬가지로 많이 사용되는 관계이다.
    
    매핑하는 방법이 **@ManyToMany**를 이용하는 방법, **@ManyToOne**을 이용하는 방법 총 2가지가 있다.
    
    우선 **@ManyToMany**를 사용하는 방법부터 알아보자.

  ![img1 daumcdn 2](https://github.com/inu-appcenter/server-study-16th/assets/86196038/5274f3f1-776f-41a5-9f81-98c69512597c)
    
    **@ManyToMany 단방향 매핑**
    
    DB는 테이블 2개만으로 **M:N** 관계를 만들 수 없다.
    
    때문에 **@ManyToMany** 어노테이션을 사용하면 **연결 테이블**을 자동으로 생성해준다.
    
    ```java
    public class Member {
        @Id @GeneratedValue
        private Long id;
        private String name;
    
        @ManyToMany
        @JoinTable(name = "member_team")
        private List<Team> teamList;
    }
    ```
    
    - **@ManyToMany**: **M:N** 매핑
    - **@JoinTable**: 연결 테이블의 이름
    
    ---
    
    **@ManyToMany 양방향 매핑**
    
    ```java
    public class Team {
        @Id @GeneratedValue
        private Long id;
        private String name;
    
        @ManyToMany(mappedBy = "teamList")
        private List<Member> memberList;
    }
    ```
    
    - **@ManyToMany**: **M:N** 매핑
    - **mappedBy**: **N:1** 관계에서 봤듯이 Member 테이블의 Team 리스트 변수를 넣으면 된다.
    
    ---
    
    **@ManyToOne을 사용한 M:N 관계 매핑**
    
    **@ManyToMany**를 사용할 경우 어노테이션만 넣으면 JPA에서 자동으로 **연결 테이블**을 생성해주기에 편리하지만, 다음과 같은 단점들이 존재한다.
    
    - **추가적인 정보를 넣을 수 없다.** 예를 들어, member_team 테이블에 Member가 Team에 가입한 날짜를 저장하고 싶어도, JPA가 자동으로 생성하는 테이블이기 때문에 넣을 수 없다.
    - **예상할 수 없는 쿼리가 실행된다.** JPA가 연결 테이블을 자동 생성함으로써 그 테이블은 JPA에서 자동으로 관리해주기 때문에, CRUD를 할 때 중간 테이블을 거치는 쿼리 또한 자동으로 실행시켜버린다.
    
    간단한 프로젝트에선 쓸만 하지만, **Entity**간의 관계가 복잡한 실무에서는 쓰지 않는것이 좋다.
    
    **@ManyToMany** 대신 연결 테이블을 **Entity**로 승격하여 직접 생성하고, **@ManyToOne** 양방향 관계로 직접 매핑해주자.
    
    우선 **연결 테이블**을 선언해준다.
    
    **Entity**로 승격되었기 때문에 가입 일자 같은 추가적인 데이터가 들어갈 수 있다.
    
    **(member_id, team_id)**로 **Primary Key**를 선언할 수도 있지만, 제약조건이 많아질 경우 독립적인 ID가 있는 것이 유연하게 대처하기 좋기 때문에 별도의 ID를 만드는 것이 좋다.
    
    ```java
    @Entity
    public class MemberTeam {
        @Id @GeneratedValue
        private Long id;
    
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "member_id")
        private Member member;
    
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "team_id")
        private Team team;
    
        private LocalDateTime joinDate;
    }
    ```
    
    그 다음 Member, Team의 **@ManyToMany**를 **연결 테이블**에 대한 **@OneToMany**로 변경해준다.
    
    ```java
    public class Member {
        @Id @GeneratedValue
        private Long id;
        private String name;
    
        @OneToMany(mappedBy = "member")
        private List<MemberTeam> teamList;
    }
    ```
    
    ```java
    public class Team {
        @Id @GeneratedValue
        private Long id;
        private String name;
    
        @OneToMany(mappedBy = "team")
        private List<MemberTeam> memberList;
    }
    ```
    
    ---
    
- **연관관계 편의 메소드**
    
    연관관계가 매핑 되었더라도 객체 상태일 때를 고려하여 양쪽 모두에 값을 설정해야 한다.
    
    예를 들어 Member와 Team을 조회했는데, Team에 Member를 추가하고 Member에 아무런 설정도 해주지 않으면 Team에는 Member가 들어가있지만, Member 입장에서는 Team이 NULL인 상황이 된다.
    
    이런 현상을 방지하기 위해 **편의 메소드**를 작성하면 좋다.
    
    코드가 복잡하지 않기 때문에 보기만 해도 이해가 될 것이다.
    
    ```java
    public class Member {
        @Id @GeneratedValue
        private Long id;
        private String name;
    
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "team_id")
        private Team team;
    
        public void changeTeam(Team team) {
            if(team != null) {
                team.getMemberList().add(this);
                this.team = team;
            }
        }
    }
    ```
    
    ```java
    public class Team {
        @Id @GeneratedValue
        private Long id;
        private String name;
    
        @OneToMany(mappedBy = "team")
        private List<Member> memberList = new ArrayList<>();
    
        public void addMember(Member member) {
            memberList.add(member);
            member.changeTeam(this);
        }
    }
    ```
    
    코드를 보면 **changeTeam / addMember** 함수를 호출할 때 자신의 값만 설정하는 것이 아니라 상대편의 값도 같이 설정하는 것을 볼 수 있다.
    
    이렇게 **연관관계 편의 메소드**를 정의해놓고 사용하면 데이터에 모순이 생기는 것을 방지할 수 있다.
    

# 영속성 전이란?

### 영속성?

> 영속성이란 데이터를 생성한 프로그램이 종료되더라도 사라지지 않는 데이터의 특성을 말한다.
> 

### 영속성 전이

> **영속성 전이(CASCADE)는** 데이터베이스의 관계형 데이터베이스 관리 시스템에서 사용되는 개념으로, **부모 엔티티의 영속성 상태 변화가 자식 엔티티에도 영향을 미치는 것**을 말한다.
> 

JPA에서는 다양한 종류의 Cascade 옵션을 제공하여 영속성 전이 동작을 지원한다. 

- Cascade 옵션
    1. **CascadeType.ALL** : 모든 상태 변화를 전이한다. 해당 엔티티의 PERSIST, MERGE, REMOVE, REFRESH, DETACH 등의 모든 작업이 연관된 엔티티에도 적용된다.
    2. **CascadeType.PERSIST** : 엔티티가 영속 상태로 전이될 때, 해당 엔티티의 상태 변화를 연관된 엔티티에도 전이시킨다.
    3. **CascadeType.MERGE :** 엔티티의 변경이 병합될 때(merge) 해당 엔티티의 상태 변화를 연관된 엔티티에도 전이시킨다.
    4. **CascadeType.REMOVE** : 엔티티가 삭제될 때, 해당 엔티티의 상태 변화를 연관된 엔티티에도 전이시킨다.
    5. **CascadeType.REFRESH** : 엔티티를 새로고침(refresh)할 때, 해당 엔티티의 상태 변화를 연관된 엔티티에도 전이시킨다.
    6. **CascadeType.DETACH** : 엔티티를 분리(detach)할 때, 해당 엔티티의 상태 변화를 연관된 엔티티에도 전이시킨다.
    7. **CascadeType.NONE** : 어떤 상태 변화도 전이하지 않는다. 연관된 엔티티의 상태는 변경되지 않는다.

CASCADE 옵션을 사용할 때 주의해야 할 점도 있다.

1. 참조 무결성 제약조건 위반 가능
    
    CascadeType.REMOVE 혹은 CascadeType.ALL 옵션을 사용하면 엔티티 삭제 시 연관된 엔티티들이 전부 삭제가 되기 때문에 의도치 않게 참조 무결성 제약조건을 위반할 수도 있다.
    
    > *참조 무결성 제약조건이란, 관계형 데이터베이스(RDB)에서 릴레이션(relation)은 참조할 수 없는 외래 키(foreign key)를 가져서는 안 된다는 조건을 의미한다.*
    > 
    > 
    > *이를 위반하는 경우 데이터의 모순이 발생하게 된다.*
    > 
2. 양방향 연관관계 매핑 시 충돌 가능
    
    Comment, Post가 다대일 관계로 구성되어 있고 연관관계의 주인은 Comment라고 가정할 때 아래와 같이 Post의 comments 필드에 CascadeType.PERSIST를 지정하는 경우 문제가 생길 수 있다.
    
    ```java
    // Post.java
    @Entity
    public class Post {
    
        @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
        private List<Comment> comments = new ArrayList<>();
    ```
    
    ```java
    @Test
    void bidirectional_bad_case() {
        Post post = new Post();
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
    
        post.addComment(comment1);
        post.addComment(comment2);
    
        commentRepository.delete(comment1);
        postRepository.save(post);
    
        assertThat(commentRepository.existsById(comment1.getId())).isTrue();
      }
    ```
    
    위 테스트 코드에서 `commentRepository.delete(comment1)`을 호출했음에도 삭제가 되지 않는다. `delete` 를 호출해 comment1이 삭제된 상태에서 post를 `save` 하니 다시 comment1 값이 복원된 것이다.
    
    이처럼 영속화에 대한 관리 지점이 두 곳이면 데이터 값을 예측할 수 없는 문제가 생긴다.
    

그러면 영속성 전이는 언제 사용해야 하는가?

하나의 부모가 자식들을 관리할 때 의미가 있다 (= 소유자가 하나일 때)

만약, 다른 엔티티와 자식이 관계가 있다면 쓰지 않고 따로 관리해야 한다.

> 조건 두 가지 모두 만족할 때 사용한다.
> 
> 
> 1. 부모와 자식의 라이프 사이클 거의 유사할 때
> 
> 2. 단일 소유자 일 때
> 

### 영속성 전이의 활용

실제로 영속성 전이를 사용하는 예제를 확인해보자

**영속성 전이 : 저장**

![image 4](https://github.com/inu-appcenter/server-study-16th/assets/86196038/598a5729-2918-42e8-bc5e-09fd51438ec9)

```java
@Entity
public class Parent {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "parent")
    private List<Child> childList = new ArrayList<>();
}
```

```java
@Entity
public class Child {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;
}
```

```java
Parent parent = new Parent();
em.persist(parent);

Child child1 = new Child();
child1.setParent(parent);
parent.getChildList().add(child1);
em.persist(child1);             // 자식1 저장

Child child2 = new Child();
child2.setParent(parent);
parent.getChildList().add(child1);
em.persist(child2);             // 자식2 저장
```

위의 예제에서는 자식 엔티티도 직접 영속화해줘야 했다. 

하지만 Parent 클래스에서 이렇게 `CascadeType.PERSIST` 로 설정하게 되면, 해당 부모 엔티티와 연관된 자식 엔티티들도 같이 영속성 컨텍스트에 저장된다.

```java
@Entity
public class Parent {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST)
    private List<Child> childList = new ArrayList<>();
}
```

```java
Parent parent = new Parent();

Child child1 = new Child();
child1.setParent(parent);
parent.getChildList().add(child1);

Child child2 = new Child();
child2.setParent(parent);
parent.getChildList().add(child2);

em.persist(parent);
```

**영속성 전이 : 삭제**

```java
Parent findParent = em.find(Parent.class, 1L);
Child findChild1 = em.find(Child.class, 1L);
Child findChild2 = em.find(Child.class, 2L);

em.remove(findParent);
em.remove(findChild1);
em.remove(findChild2);
```

영속성 전이 기능 없이 부모 엔티티와 자식 엔티티를 모두 제거하려면 위처럼 각각의 엔티티를 하나씩 제거해야 한다.

하지만, `CascadeType.REMOVE`를 활용한 영속성 전이를 활용한다면, 부모 엔티티만 삭제함으로써 연관된 자식 엔티티도 함께 삭제할 수 있다.

```java
Parent findParent = em.find(Parent.class, 1L);

em.remove(findParent);
```

**고아 객체**

JPA는 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제하는 기능을 제공하는데, 이를 **고아 객체(ORPHAN) 제거** 라고 한다.

이 기능을 사용하면, **부모 엔티티의 컬렉션에서 자식 엔티티의 참조만 제거하면 자식 엔티티가 자동으로 삭제되도록 할 수 있다.**

- `orphanRemoval = true` 옵션을 통해 컬렉션에서 엔티티를 제거하면 데이터베이스의 데이터도 삭제된다.
    
    ```java
    @Entity
    public class Parent {
        @Id @GeneratedValue
        private Long id;
        private String name;
    
        @OneToMany(mappedBy = "parent", orphanRemoval = true)
        private List<Child> childList = new ArrayList<>();
    }
    ```
    
    ```java
    Parent findParent = em.find(Parent.class, 1L);
    findParent.getChildList().remove(0);
    ```
    

고아 객체 제거는 **참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 보고, 삭제하는 기능**이다. 따라서 이 기능은 참조하는 곳이 하나일 때만 사용해야 한다
그리고 고아 객체 제거에는 기능이 한 가지 더 있다. 개념적으로 볼 때, **부모를 제거하면 자식은 고아가 된다. 따라서 부모를 제거하면 자식도 같이 제거된다. 따라서, 고아 객체 제거 기능을 사용하면 `CascadeType.REMOVE` 기능도 포함되어 있다**

**영속성 전이 + 고아 객체 제거, 생명 주기**

일반적으로 엔티티는 EntityManager.persist()를 통해 영속화되고 EntityManager.remove()를 통해 제거된다.

이것은 엔티티 스스로 생명주기를 관리한다는 뜻이다.

그런데 **두 옵션(`CascadeType.ALL` 과 `orphanRemoval = true`)을 모두 활성화하면, 부모 엔티티를 통해서 자식의 생명주기를 관리할 수 있다.** 예를 들면 다음과 같다.

```java
// 자식을 저장하려면 부모에 등록만 하면 된다 (CASCADE.PERSIST)
Parent parent = em.find(Parent.class, 1L);
parent.addChild(child1);
```

```java
// 자식을 삭제하려면, 부모에서 제거하면 된다 (orphanRemoval)
Parent parent = em.find(Parent.class, 1L);
parent.getChildList.remove(child1);
```