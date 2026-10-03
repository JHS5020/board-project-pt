package com.example.boardproject.post;

import java.util.List;

/**
 * QueryDSL로 구현할 커스텀 쿼리 메서드들의 "시그니처(규격)"만 정의하는 인터페이스.
 * 실제 구현은 PostRepositoryImpl에서 합니다.
 */
public interface PostRepositoryCustom {

    /**
     * 제목, 작성자 닉네임으로 검색하는 동적 쿼리.
     * title, nickname 둘 다 null이면 조건 없이 전체 조회,
     * 값이 있으면 해당 조건이 쿼리에 추가됩니다. (삭제된 글은 항상 제외)
     */
    List<Post> search(String title, String nickname);
}