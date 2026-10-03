package com.example.boardproject.common;

import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 인증은 되었지만 권한이 부족한 사용자가 접근했을 때 Spring Security가 호출하는 핸들러.
 * 401(누구인지 모름)과 달리 403(누구인지는 알지만 권한 없음)을 표준 에러 포맷으로 응답합니다.
 */
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Forbidden");
        body.put("message", "접근 권한이 없습니다.");
        body.put("status", 403);

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}