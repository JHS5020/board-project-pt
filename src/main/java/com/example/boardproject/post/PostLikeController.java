package com.example.boardproject.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    /** 추천. 로그인 필수(Security가 막아줌). 성공 시 201 Created. */
    @PostMapping
    public ResponseEntity<Void> like(@PathVariable Long postId, Authentication authentication) {
        postLikeService.like(postId, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /** 추천 취소. 로그인 필수. 성공 시 204 No Content. */
    @DeleteMapping
    public ResponseEntity<Void> unlike(@PathVariable Long postId, Authentication authentication) {
        postLikeService.unlike(postId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}