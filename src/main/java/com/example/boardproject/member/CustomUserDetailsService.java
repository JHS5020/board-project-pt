package com.example.boardproject.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security가 인증 과정에서 사용자 정보를 조회할 때 호출하는 인터페이스의 구현체.
 *
 * 동작 흐름:
 *   1. 로그인 요청이 들어오면 Security가 이 클래스의 loadUserByUsername()을 호출
 *   2. 우리 DB에서 Member를 찾아서 Security가 이해할 수 있는 UserDetails 객체로 변환
 *   3. Security가 UserDetails의 비밀번호(암호화된 값)와
 *      사용자가 입력한 비밀번호(암호화해서)를 비교
 *   4. 일치하면 인증 성공 -> SecurityContext에 저장
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다: " + username));

        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword()) // 이미 암호화된 값 - Security가 알아서 비교해줌
                .authorities(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()))
                .build();
    }
}