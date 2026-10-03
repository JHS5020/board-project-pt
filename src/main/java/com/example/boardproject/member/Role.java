package com.example.boardproject.member;

/**
 * 회원 권한.
 *
 * Enum으로 만드는 이유:
 *   - 문자열("USER", "ADMIN")을 그냥 쓰면 오타가 나도 컴파일러가 못 잡아줍니다.
 *   - Enum은 정해진 값 외에는 아예 존재할 수 없으니 타입 안정성이 보장됩니다.
 *
 * Spring Security 권한 문자열은 관례상 "ROLE_" 접두사를 붙입니다.
 * (hasRole("USER")라고 쓰면 Security 내부에서 자동으로 "ROLE_USER"를 찾습니다 -
 *  이 접두사 규칙을 모르면 나중에 "권한이 있는데 왜 인증이 안 되지?" 하고 헤매게 됩니다)
 */
public enum Role {
    USER,
    ADMIN
}