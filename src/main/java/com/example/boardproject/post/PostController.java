package com.example.boardproject.post;

import com.example.boardproject.common.PageResponse;
import com.example.boardproject.post.dto.PostCreateRequest;
import com.example.boardproject.post.dto.PostListResponse;
import com.example.boardproject.post.dto.PostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /** 게시글 작성. 로그인 필수(Security가 막아줌)라서 authentication은 항상 존재한다. */
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody PostCreateRequest request,
                                       Authentication authentication) {
        Long postId = postService.create(authentication.getName(), request);

        // 201 Created + Location 헤더: 현재 요청 주소(/api/posts) 뒤에 /{postId}를 붙인 URI
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(postId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /** 게시글 단건 조회. 비로그인도 도달하며, 회원 전용 글 여부는 서비스가 판단한다. */
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId,
                                                Authentication authentication) {
        return ResponseEntity.ok(postService.getPost(postId, isLoggedIn(authentication)));
    }

    /** 게시글 목록 조회. 기본: 최신순(id 내림차순) 10개씩. 예) /api/posts?page=0&size=10 */
    @GetMapping
    public ResponseEntity<PageResponse<PostListResponse>> getPosts(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(postService.getPosts(pageable)));
    }

    /**
     * 로그인 여부 판별. 비로그인이면 authentication이 null이거나 AnonymousAuthenticationToken(익명 토큰)일 수 있어서
     * 두 경우를 모두 방어한다.
     */
    private boolean isLoggedIn(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}