package com.example.boardproject.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import java.util.LinkedHashMap;

/**
 * @RestControllerAdvice: 모든 @RestController에서 발생하는 특정 예외들을
 *      한곳에서 가로채서 일관된 형식으로 응답하게 해주는 전역 처리기입니다.
 *      각 컨트롤러/서비스마다 try-catch를 따로 두지 않아도, 여기 한 곳만 관리하면 됩니다.
 *
 * 이게 없으면 지금처럼 예외가 처리 안 된 채 흘러나가면서 Spring이나 서블릿 컨테이너의
 * 기본 에러 응답(형식이 우리 API와 안 맞고, 스택 트레이스 등 불필요한 정보가 노출될 수 있음)이
 * 그대로 내려갑니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 우리가 직접 정의한 BusinessException을 처리합니다.
     * (예: "이미 사용 중인 아이디입니다" 같은 검증 실패)
     * 클라이언트 쪽 잘못된 요청이므로 400 Bad Request로 응답합니다.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildErrorBody(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    /**
     * @Valid 검증 실패 시(예: username이 비어있거나 너무 짧을 때) 발생하는
     * MethodArgumentNotValidException을 처리합니다.
     * 필드별로 어떤 검증에 왜 실패했는지 메시지를 모아서 응답합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> body = buildErrorBody(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.");
        body.put("fieldErrors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * 위에서 처리하지 못한 나머지 모든 예외(우리가 예상 못한 버그 등)를 최종적으로 잡습니다.
     * 실제 원인은 로그로 남기되, 클라이언트에게는 내부 구현이 노출되지 않도록
     * 일반적인 메시지만 500으로 응답합니다. (스택 트레이스를 그대로 노출하면 보안상 위험합니다)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        // 실무에서는 이 지점에 로깅 프레임워크(Slf4j 등)로 e.printStackTrace() 대신
        // log.error("예상하지 못한 에러", e); 같은 로그를 남깁니다. (다음 단계에서 다룰 예정)
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Unauthorized");
        body.put("message", "아이디 또는 비밀번호가 올바르지 않습니다.");
        body.put("status", 401);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }


    private Map<String, Object> buildErrorBody(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return body;
    }
}