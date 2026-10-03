package com.example.boardproject.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * QueryDSL을 쓰기 위한 핵심 도구, JPAQueryFactory를 스프링 빈으로 등록하는 설정 클래스.
 *
 * JPAQueryFactory는 QueryDSL 쿼리를 실제로 만들고 실행하는 역할을 합니다.
 * EntityManager(JPA가 DB와 상호작용하는 핵심 객체)를 필요로 하기 때문에,
 * 이 클래스에서 EntityManager를 주입받아 JPAQueryFactory를 생성해줍니다.
 *
 * 이렇게 한번 Bean으로 등록해두면, 이후 Repository 구현체들에서
 * @RequiredArgsConstructor로 JPAQueryFactory를 그냥 주입받아 쓸 수 있게 됩니다.
 */
@Configuration
public class QuerydslConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }
}