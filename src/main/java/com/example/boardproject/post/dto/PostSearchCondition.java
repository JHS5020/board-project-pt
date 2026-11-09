package com.example.boardproject.post.dto;

import com.example.boardproject.post.PostSearchType;
import com.example.boardproject.post.PostSortType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** 목록 검색 조건을 한 덩어리로 묶은 객체. 조건이 늘어나도 메서드 시그니처가 안 변한다. */
@Getter
@AllArgsConstructor
public class PostSearchCondition {

    private final PostSearchType type;
    private final String keyword;
    private final PostSortType sortType;
    private final boolean loggedIn;   // 비로그인일 때 회원 전용 글 본문 검색을 막기 위해 필요
}