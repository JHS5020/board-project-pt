package com.example.boardproject.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 여러 엔티티(Member, Post, Comment)가 공통으로 갖는 필드를 모아둔 부모 클래스.
 *
 * @MappedSuperclass: 이 클래스 자체는 테이블로 만들어지지 않고,
 *                     상속받는 자식 엔티티의 테이블에 필드들만 컬럼으로 추가되는 방식입니다.
 *                     (참고로 @Entity를 붙이는 상속 전략도 있지만, 학습 단계에서는
 *                     이 방식이 훨씬 단순하고 실무에서도 이런 '공통 필드 상속'용으로는
 *                     MappedSuperclass가 표준적으로 쓰입니다.)
 *
 * @EntityListeners(AuditingEntityListener.class):
 *      엔티티가 저장되거나 수정될 때 Spring Data JPA가 자동으로 감시(Auditing)해서
 *      아래 @CreatedDate, @LastModifiedDate 필드를 알아서 채워주도록 등록하는 것.
 *      이 기능을 쓰려면 메인 애플리케이션 클래스에 @EnableJpaAuditing을 추가해야 동작합니다.
 *      (다음 단계에서 추가할 예정)
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false) // 최초 생성 시점 값은 이후 절대 변경되지 않도록 막음
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}