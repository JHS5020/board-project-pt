package com.example.boardproject.post.dto;

import com.example.boardproject.post.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 작성 요청 바디.
 * 작성자(author)는 일부러 받지 않는다. 바디로 받으면 남의 이름으로 글을 쓸 수 있으므로
 * 로그인한 사용자 정보를 서버가 세션에서 꺼내 사용한다.
 */
@Getter
@NoArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자 이하로 입력해주세요.")
    private String title;

    // DB 컬럼이 TEXT(최대 65,535바이트)라서, 한글(글자당 3바이트)을 고려해 넉넉하게 제한
    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 10000, message = "내용은 10000자 이하로 입력해주세요.")
    private String content;

    // 생략하면 null → Post 생성자에서 PUBLIC으로 처리
    private Visibility visibility;
}