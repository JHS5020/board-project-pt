package com.example.boardproject.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.boardproject.member.QMember.member;
import static com.example.boardproject.post.QPost.post;

/**
 * 이름 규칙 중요: 반드시 "PostRepositoryCustom을 구현하는 클래스 이름"이
 * "PostRepository" + "Impl" 이어야 Spring Data JPA가 자동으로 인식해서
 * PostRepository와 연결해줍니다. (Spring Data JPA의 관례 - 어노테이션 없이 이름만으로 동작)
 */
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    // QuerydslConfig에서 등록해둔 빈을 생성자 주입으로 받습니다.
    // (@RequiredArgsConstructor가 final 필드를 파라미터로 받는 생성자를 자동 생성해줌)
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> search(String title, String nickname) {
        return queryFactory
                .selectFrom(post)                        // SELECT * FROM post
                .join(post.author, member)                // JOIN member (post.author와 연관관계로 조인)
                .where(
                        titleContains(title),             // 조건들을 메서드로 분리 (아래 설명)
                        nicknameEq(nickname),
                        notDeleted()                       // 항상 붙는 고정 조건 - 삭제된 글 제외
                )
                .fetch();                                  // 실제 쿼리 실행 후 리스트로 반환
    }

    /**
     * 아래처럼 조건을 별도 private 메서드로 쪼개는 게 QueryDSL의 핵심 패턴입니다.
     *
     * BooleanExpression을 리턴 타입으로 쓰는 이유:
     *   .where() 안에 값을 콤마로 나열하면, QueryDSL이 자동으로 AND로 묶어줍니다.
     *   그리고 null을 리턴하면 그 조건은 자동으로 무시됩니다 (SQL에 아예 안 들어감).
     *
     * 즉, "title이 없으면 조건 자체를 안 넣고, 있으면 LIKE 조건을 넣는다"를
     * if문 없이 매우 깔끔하게 표현할 수 있습니다. 이게 메서드 이름 쿼리로는
     * 사실상 불가능했던 부분이에요.
     */
    private BooleanExpression titleContains(String title) {
        return (title == null || title.isBlank()) ? null : post.title.contains(title);
    }

    private BooleanExpression nicknameEq(String nickname) {
        return (nickname == null || nickname.isBlank()) ? null : member.nickname.eq(nickname);
    }

    private BooleanExpression notDeleted() {
        return post.deletedAt.isNull();
    }
}