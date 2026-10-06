package com.example.boardproject.post.dto;

import com.example.boardproject.post.Post;
import com.example.boardproject.post.Visibility;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 게시글 상세 응답.
 * 필드를 final로 두고 생성자로만 값을 넣어서, 만들어진 뒤에는 바뀌지 않는 객체로 쓴다.
 */
@Getter
@AllArgsConstructor
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String authorNickname;
    private final Visibility visibility;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /** 엔티티 → 응답 변환. author는 LAZY라서 반드시 트랜잭션 안(서비스)에서 호출해야 한다. */
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getNickname(),
                post.getVisibility(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}