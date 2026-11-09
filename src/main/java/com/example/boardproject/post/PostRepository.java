package com.example.boardproject.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    // DB에게 "현재 값에서 +1" 시키는 벌크 UPDATE → 동시에 눌러도 갱신 손실 없음
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Post p set p.likeCount = p.likeCount + 1 where p.id = :postId")
    void increaseLikeCount(@Param("postId") Long postId);

    // 0 아래로 내려가지 않도록 방어 조건 추가
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Post p set p.likeCount = p.likeCount - 1 where p.id = :postId and p.likeCount > 0")
    void decreaseLikeCount(@Param("postId") Long postId);
}