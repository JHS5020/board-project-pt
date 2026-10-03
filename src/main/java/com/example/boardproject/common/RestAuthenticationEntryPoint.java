package com.example.boardproject.common;

import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 인증되지 않은 사용자가 보호된 API에 접근했을 때 Spring Security가 호출하는 진입점.
 * 설정을 안 해주면 기본값(Http403ForbiddenEntryPoint)이 무조건 403을 내려버리기 때문에,
 * 여기서 직접 401 + 우리 프로젝트의 표준 에러 포맷으로 응답합니다.
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Unauthorized");
        body.put("message", "로그인이 필요합니다.");
        body.put("status", 401);

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}