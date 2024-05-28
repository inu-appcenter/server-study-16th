package com.appcenter.practice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/*
@SpringBootApplication이 붙은 스프링 부트 메인 클래스에 @EnableJpaAuditing 를 사용하면,
스프링 컨테이너가 필요한 테스트를 할 때,
@EnableJpaAuditing 가 붙어 있으면 JPA 관련 빈을 불러와야 하는데 mock 테스트를 할 때 해당 빈이 없어서 호출 할 수 없기 때문에
별도 설정파일로 분리해서 사용한다.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
