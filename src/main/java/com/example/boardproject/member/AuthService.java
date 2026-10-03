package com.example.boardproject.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/*
 * 로그인 흐름:
 *   1. 사용자가 입력한 username/password로 인증 토큰(아직 검증 전)을 만듦
 *   2. AuthenticationManager.authenticate()가 내부적으로
 *      CustomUserDetailsService.loadUserByUsername() 호출 → PasswordEncoder로 비밀번호 비교
 *   3. 성공하면 검증된 Authentication 객체가 반환됨
 *   4. 이 Authentication을 SecurityContext에 넣고, 그 SecurityContext를
 *      HttpSession에 수동으로 저장 → 이후 요청부터는 세션 쿠키로 이 정보를 계속 읽어옴
 *
 * 폼 로그인(formLogin)을 썼다면 이 과정을 Spring Security가 필터에서 자동으로 해주는데,
 * 지금은 JSON 기반 REST API라서 이 과정을 직접 코드로 수행하는 것입니다.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    public void login(String username, String password, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // 여기서 실패하면 AuthenticationException 계열 예외가 터짐
        // (아이디 없음 -> UsernameNotFoundException, 비밀번호 틀림 -> BadCredentialsException)
        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // SecurityContext를 세션에 저장 - 이 키 이름은 Spring Security가 내부적으로 인식하는 고정된 이름
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);
    }
}