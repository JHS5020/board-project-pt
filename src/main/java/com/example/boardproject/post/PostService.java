package com.example.boardproject.post;

import com.example.boardproject.common.NotFoundException;
import com.example.boardproject.common.UnauthorizedException;
import com.example.boardproject.member.Member;
import com.example.boardproject.member.MemberRepository;
import com.example.boardproject.post.dto.PostCreateRequest;
import com.example.boardproject.post.dto.PostListResponse;
import com.example.boardproject.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 게시글 비즈니스 로직.
 * 클래스 기본은 읽기 전용 트랜잭션, 쓰기가 필요한 메서드만 @Transactional로 다시 열어준다. (MemberService와 동일 패턴)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /** 게시글 작성. 작성자는 요청 바디가 아니라 로그인한 사용자(username)로 결정한다. */
    @Transactional
    public Long create(String username, PostCreateRequest request) {
        Member author = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("로그인 정보가 올바르지 않습니다. 다시 로그인해주세요."));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .visibility(request.getVisibility()) // null이면 Post 생성자에서 PUBLIC 처리
                .build();

        return postRepository.save(post).getId();
    }

    /** 게시글 단건 조회. 회원 전용 글은 로그인한 사용자만 볼 수 있다. */
    public PostResponse getPost(Long postId, boolean loggedIn) {
        Post post = postRepository.findById(postId)
                .filter(p -> !p.isDeleted()) // 삭제된 글은 "없는 글"과 똑같이 취급 → 404
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.canBeReadBy(loggedIn)) {
            throw new UnauthorizedException("회원 전용 게시글입니다. 로그인이 필요합니다.");
        }

        return PostResponse.from(post); // author는 LAZY → 반드시 이 트랜잭션 안에서 변환
    }

    /** 게시글 목록 조회. 회원 전용 글도 제목은 보이고, 본문(content)은 목록 DTO에 아예 없다. */
    public Page<PostListResponse> getPosts(Pageable pageable) {
        return postRepository.findByDeletedAtIsNull(pageable)
                .map(PostListResponse::from);
    }
}