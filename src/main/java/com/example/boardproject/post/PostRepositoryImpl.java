package com.example.boardproject.post;

import com.example.boardproject.post.dto.PostSearchCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.boardproject.member.QMember.member;
import static com.example.boardproject.post.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> search(PostSearchCondition condition, Pageable pageable) {

        // 1) 내용 조회: author를 fetch join → 목록 변환 시 N+1 없음 (기존 @EntityGraph와 같은 목적)
        List<Post> content = queryFactory
                .selectFrom(post)
                .join(post.author, member).fetchJoin()
                .where(
                        post.deletedAt.isNull(),
                        keywordCondition(condition)   // null이면 where에서 알아서 무시됨
                )
                .orderBy(orderSpecifiers(condition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2) 전체 개수 조회: 개수만 필요하니 join 없이
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        post.deletedAt.isNull(),
                        keywordCondition(condition)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    /** keyword가 없으면 null(=조건 없음). 있으면 type에 따라 조건 생성. */
    private BooleanExpression keywordCondition(PostSearchCondition condition) {
        if (!StringUtils.hasText(condition.getKeyword())) {
            return null;
        }
        String keyword = condition.getKeyword().trim();

        BooleanExpression titleMatch = post.title.containsIgnoreCase(keyword);

        return switch (condition.getType()) {
            case TITLE -> titleMatch;
            case TITLE_CONTENT -> titleMatch.or(contentMatch(keyword, condition.isLoggedIn()));
        };
    }

    /** 비로그인이면 PUBLIC 글의 본문만 검색 대상 (회원 전용 본문 추측 방지). */
    private BooleanExpression contentMatch(String keyword, boolean loggedIn) {
        BooleanExpression match = post.content.containsIgnoreCase(keyword);
        return loggedIn ? match : match.and(post.visibility.eq(Visibility.PUBLIC));
    }

    /** 정렬 기준은 여기 한 곳에서만 관리. 인기순은 case 하나만 추가하면 된다. */
    private OrderSpecifier<?>[] orderSpecifiers(PostSearchCondition condition) {
        return switch (condition.getSortType()) {
            case LATEST -> new OrderSpecifier<?>[]{ post.id.desc() };
        };
    }
}