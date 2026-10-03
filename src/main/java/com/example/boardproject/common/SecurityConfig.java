package com.example.boardproject.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 비밀번호 암호화에 쓰는 PasswordEncoder를 스프링 빈으로 등록합니다.
 *
 * BCryptPasswordEncoder를 쓰는 이유:
 *   - 단방향 해시 함수라서 암호화된 값에서 원래 비밀번호를 역산할 수 없습니다.
 *     (AES 같은 양방향 암호화와 다른 점 - 비밀번호는 절대 복호화될 필요가 없으므로 단방향이 맞습니다)
 *   - 같은 비밀번호를 두 번 암호화해도 매번 다른 결과가 나옵니다 (내부적으로 salt를 자동으로 섞어 넣음).
 *     그래서 DB가 털려도 같은 비밀번호를 쓰는 회원들을 한번에 찾아낼 수 없습니다.
 *   - 의도적으로 계산이 느리게 설계되어 있어서, 무차별 대입 공격(brute-force)에 강합니다.
 *   - Spring Security의 사실상 표준 구현체입니다.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}