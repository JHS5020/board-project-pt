package com.example.boardproject.post;

import com.example.boardproject.post.dto.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * QueryDSL로 구현할 커스텀 쿼리 메서드들의 "시그니처(규격)"만 정의하는 인터페이스.
 * 실제 구현은 PostRepositoryImpl에서 합니다.
 */
public interface PostRepositoryCustom {

    /** 조건에 맞는 글을 페이징해서 조회. 삭제된 글은 항상 제외. */
    Page<Post> search(PostSearchCondition condition, Pageable pageable);
}