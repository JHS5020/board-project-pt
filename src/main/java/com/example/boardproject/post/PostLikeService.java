package com.example.boardproject.post;

import com.example.boardproject.common.ConflictException;
import com.example.boardproject.common.NotFoundException;
import com.example.boardproject.common.UnauthorizedException;
import com.example.boardproject.member.Member;
import com.example.boardproject.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 게시글 추천 비즈니스 로직.
 * post_like 행 추가/삭제와 post.like_count 증감은 반드시 한 트랜잭션에서 같이 처리한다.
 * (둘 중 하나만 성공하면 기록과 추천수가 어긋나기 때문)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /** 추천. 이미 추천했으면 409. */
    @Transactional
    public void like(Long postId, String username) {
        Post post = findActivePost(postId);
        Member member = findMember(username);

        if (postLikeRepository.existsByPostIdAndMemberId(postId, member.getId())) {
            throw new ConflictException("이미 추천한 게시글입니다.");
        }

        postLikeRepository.save(PostLike.builder().post(post).member(member).build());
        postRepository.increaseLikeCount(postId);
    }

    /** 추천 취소. 추천한 적이 없으면 404. */
    @Transactional
    public void unlike(Long postId, String username) {
        findActivePost(postId);   // 없는/삭제된 글이면 여기서 404
        Member member = findMember(username);

        PostLike postLike = postLikeRepository.findByPostIdAndMemberId(postId, member.getId())
                .orElseThrow(() -> new NotFoundException("추천하지 않은 게시글입니다."));

        postLikeRepository.delete(postLike);
        postRepository.decreaseLikeCount(postId);
    }

    private Post findActivePost(Long postId) {
        return postRepository.findById(postId)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
    }

    private Member findMember(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("로그인 정보가 올바르지 않습니다. 다시 로그인해주세요."));
    }
}