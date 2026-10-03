package com.example.boardproject.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청 바디를 받는 DTO.
 *
 * 검증 어노테이션(@NotBlank 등)은 Controller에서 @Valid와 함께 쓰이면
 * Spring이 요청 처리 전에 자동으로 검증해서, 실패 시 400 에러를 던져줍니다.
 */
@Getter
@NoArgsConstructor
public class MemberSignUpRequest {

        @NotBlank(message = "아이디는 필수입니다.")
        @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력해주세요.")
        private String username;

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 100, message = "비밀번호는 8자 이상 입력해주세요.")
        private String password;

        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(min = 2, max = 30, message = "닉네임은 2자 이상 30자 이하로 입력해주세요.")
        private String nickname;

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;
}