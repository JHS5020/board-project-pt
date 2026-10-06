package com.example.boardproject.post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository와 PostRepositoryCustom을 둘 다 상속받습니다.
 * 그래서 이 인터페이스 하나로 기본 CRUD(JpaRepository)와
 * 커스텀 동적 쿼리(PostRepositoryCustom)를 모두 쓸 수 있게 됩니다.
 */
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    // 삭제되지 않은 글만, 페이징해서 조회. author를 함께 가져와서(N+1 방지) 목록 변환 시 추가 쿼리가 없다.
    @EntityGraph(attributePaths = "author")
    Page<Post> findByDeletedAtIsNull(Pageable pageable);
}