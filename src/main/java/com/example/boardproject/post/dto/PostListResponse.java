package com.example.boardproject.post.dto;

import com.example.boardproject.post.Post;
import com.example.boardproject.post.Visibility;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 게시글 목록 응답. 본문(content)은 의도적으로 뺐다.
 * 회원 전용 글의 본문이 목록으로 노출되는 실수를 구조적으로 막는다.
 * visibility를 같이 내려서 프론트가 회원 전용 글에 자물쇠 표시를 할 수 있게 한다.
 */
@Getter
@AllArgsConstructor
public class PostListResponse {

    private final Long id;
    private final String title;
    private final String authorNickname;
    private final Visibility visibility;
    private final LocalDateTime createdAt;

    public static PostListResponse from(Post post) {
        return new PostListResponse(
                post.getId(),
                post.getTitle(),
                post.getAuthor().getNickname(),
                post.getVisibility(),
                post.getCreatedAt()
        );
    }
}