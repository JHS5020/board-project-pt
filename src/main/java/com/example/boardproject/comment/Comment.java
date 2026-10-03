package com.example.boardproject.comment;

import com.example.boardproject.common.BaseEntity;
import com.example.boardproject.member.Member;
import com.example.boardproject.post.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 댓글 엔티티. 대댓글(중첩 댓글)을 지원하기 위해 자기 자신을 참조하는 self-referencing 구조를 씁니다.
 *
 * parent가 null이면 "최상위 댓글", 값이 있으면 "그 댓글에 달린 대댓글"이 됩니다.
 *
 * 예시 구조:
 *   댓글 A (parent = null)
 *     └ 댓글 B (parent = A)  ← A의 대댓글
 *     └ 댓글 C (parent = A)  ← A의 대댓글
 *
 * 실무 팁: 대댓글의 대댓글(3단계 이상 중첩)까지 허용할지는 서비스 정책에 따라 다릅니다.
 *          지금 구조는 이론상 무한 depth까지 가능하지만, 우리는 서비스 로직에서
 *          "최상위 댓글에만 답글을 달 수 있다"는 규칙(2단계까지만 허용)을 둘 예정입니다.
 *          (Service 단계에서 다룰 예정)
 */
@Getter
@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // 대댓글 구조: 자기 자신(Comment)을 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") // nullable 허용 (최상위 댓글은 부모가 없음)
    private Comment parent;

    private LocalDateTime deletedAt;

    @Builder
    private Comment(String content, Member author, Post post, Comment parent) {
        this.content = content;
        this.author = author;
        this.post = post;
        this.parent = parent;
    }

    public void update(String content) {
        this.content = content;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public boolean isReply() {
        return this.parent != null;
    }
}