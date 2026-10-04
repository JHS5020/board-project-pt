package com.example.boardproject.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/members/signup", "/api/auth/login", "/api/auth/logout").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(new RestAccessDeniedHandler())
                )
                /*
                 * SessionCreationPolicy.IF_REQUIRED (기본값):
                 *   "필요할 때만 세션을 생성"한다는 뜻입니다. 로그인 성공 시 우리가
                 *   직접 세션에 인증 정보를 저장하는 순간 세션이 만들어지고,
                 *   그 뒤 요청부터는 그 세션 쿠키(JSESSIONID)로 인증 상태를 식별합니다.
                 *
                 * 참고로 JWT 같은 완전 무상태(Stateless) 방식을 쓸 거라면
                 * SessionCreationPolicy.STATELESS로 설정해서 세션 자체를 아예 안 만들게 합니다.
                 * 지금은 세션 기반이므로 명시적으로 IF_REQUIRED를 적어 의도를 드러냅니다.
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                //로그아웃은 컨트롤러 없이 Security 필터가 처리함
                ).logout(logout -> logout
                        .logoutRequestMatcher(PathPatternRequestMatcher.withDefaults()
                                .matcher(HttpMethod.POST, "/api/auth/logout"))
                        .logoutSuccessHandler(new RestLogoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );


        return http.build();
    }

    /*
     * AuthenticationManager: 실제로 "아이디/비밀번호가 맞는지" 검증을 수행하는 핵심 객체.
     * 내부적으로 우리가 만든 CustomUserDetailsService + PasswordEncoder를 사용해서
     * DB에서 조회한 비밀번호(암호화됨)와 사용자가 입력한 비밀번호를 비교합니다.
     *
     * Spring Security가 AuthenticationConfiguration을 통해 이미 다 조립해서 갖고 있는데,
     * 기본적으로는 빈으로 바로 못 꺼내 쓰게 숨겨져 있어서, 이렇게 명시적으로 빈 등록을 해줘야
     * 우리 AuthService에서 주입받아 쓸 수 있습니다.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}