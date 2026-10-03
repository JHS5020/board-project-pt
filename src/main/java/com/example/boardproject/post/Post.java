package com.example.boardproject.post;

import com.example.boardproject.common.BaseEntity;
import com.example.boardproject.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 게시글 엔티티.
 *
 * @ManyToOne: 게시글(N) : 작성자(1) 관계. 게시글 여러 개가 회원 한 명에 속할 수 있다는 뜻.
 *
 * fetch = FetchType.LAZY: 연관관계 조회 전략.
 *      Post를 조회할 때 author(Member)를 항상 같이 가져오지 않고,
 *      실제로 author.getUsername() 등을 호출하는 시점에 그때서야 조회합니다(지연 로딩).
 *      기본값이 EAGER(즉시 로딩)인데, 이걸 그대로 두면 Post 하나 조회할 때마다
 *      불필요하게 Member까지 매번 같이 조회해서 성능이 나빠지는 문제가 실무에서 자주 발생합니다.
 *      그래서 @ManyToOne, @OneToOne은 기본이 EAGER라도 습관적으로 LAZY로 바꾸는 게 사실상 국룰입니다.
 */
@Getter
@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob // 긴 텍스트 저장용 (MySQL에서 TEXT 타입으로 매핑됨)
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false) // 실제 FK 컬럼명 지정
    private Member author;

    // ---- Soft Delete ----
    // 이 값이 null이 아니면 "삭제된 게시글"로 취급합니다.
    // 실제로 DB에서 행을 지우지 않고, 삭제 시각을 기록해서 삭제된 것처럼 보이게만 처리합니다.
    private LocalDateTime deletedAt;

    @Builder
    private Post(String title, String content, Member author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}