package com.example.boardproject.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JpaRepository<Member, Long>
 *   - 첫 번째 타입 파라미터(Member): 이 Repository가 다루는 엔티티 타입
 *   - 두 번째 타입 파라미터(Long): 그 엔티티의 @Id 필드 타입
 *
 * 이 인터페이스 하나만 선언하면, Spring Data JPA가 애플리케이션 실행 시점에
 * "구현체(실제 동작하는 클래스)"를 자동으로 만들어서 Spring 빈으로 등록해줍니다.
 * 우리는 구현 코드를 단 한 줄도 안 썼는데, 아래 기본 메서드들이 이미 동작합니다:
 *
 *   save(Member member)          → INSERT 또는 UPDATE
 *   findById(Long id)            → SELECT ... WHERE id = ?
 *   findAll()                    → SELECT * FROM member
 *   deleteById(Long id)          → DELETE ... WHERE id = ?
 *   count()                      → SELECT COUNT(*)
 *   existsById(Long id)          → SELECT 1 ... WHERE id = ? LIMIT 1 (있는지 여부만 확인)
 *
 * 그리고 "쿼리 메서드"라는 기능도 있습니다 - 메서드 이름을 규칙에 맞게 지으면
 * Spring이 이름을 해석해서 자동으로 쿼리를 만들어줍니다. 구현 코드 없이 선언만 하면 됩니다.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 메서드 이름 규칙: findBy + 필드명
    // Spring이 이름을 보고 "SELECT * FROM member WHERE username = ?" 쿼리를 자동 생성합니다.
    Optional<Member> findByUsername(String username);

    // findBy + 필드명 + And + 다른 필드명 → WHERE username = ? AND email = ?
    Optional<Member> findByUsernameAndEmail(String username, String email);

    // existsBy + 필드명 → 있는지 없는지만 boolean으로 확인 (회원가입 시 중복체크에 사용)
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}