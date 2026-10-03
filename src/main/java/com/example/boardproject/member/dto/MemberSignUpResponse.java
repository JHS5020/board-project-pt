package com.example.boardproject.member.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * 회원가입 성공 시 응답으로 내려줄 DTO.
 * 비밀번호 등 민감 정보 없이, 꼭 필요한 정보(생성된 회원 id)만 담습니다.
 */
@Getter
@AllArgsConstructor
public class MemberSignUpResponse {
    private final Long id;
}