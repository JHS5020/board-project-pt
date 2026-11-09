package com.example.boardproject.post;

import com.example.boardproject.common.BaseEntity;
import com.example.boardproject.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 추천 기록. "어떤 회원이 어떤 글을 추천했는지"를 한 행으로 저장한다.
 * (post_id, member_id) 유니크 제약: 같은 회원이 같은 글을 두 번 추천하는 걸 DB 차원에서 막는다.
 */
@Getter
@Entity
@Table(
        name = "post_like",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_post_like_post_member",
                columnNames = {"post_id", "member_id"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    private PostLike(Post post, Member member) {
        this.post = post;
        this.member = member;
    }
}