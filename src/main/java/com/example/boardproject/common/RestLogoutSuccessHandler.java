package com.example.boardproject.common;

import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 로그아웃 처리가 끝난 뒤 Spring Security가 호출하는 핸들러.
 * 설정을 안 하면 로그인 페이지로 리다이렉트(302)하는데, REST API에서는
 * 화면 이동이 필요 없으므로 200 + JSON으로 결과만 알려줍니다.
 */
public class RestLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {
        // authentication은 로그인 안 한 상태로 호출하면 null일 수 있음 (그래도 200 처리)
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "로그아웃되었습니다.");
        body.put("status", 200);

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}