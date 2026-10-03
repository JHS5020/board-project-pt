package com.example.boardproject.post;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository와 PostRepositoryCustom을 둘 다 상속받습니다.
 * 그래서 이 인터페이스 하나로 기본 CRUD(JpaRepository)와
 * 커스텀 동적 쿼리(PostRepositoryCustom)를 모두 쓸 수 있게 됩니다.
 */
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}