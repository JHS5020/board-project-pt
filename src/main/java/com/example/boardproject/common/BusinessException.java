package com.example.boardproject.common;

/**
 * 비즈니스 규칙 위반 시 던지는 커스텀 예외.
 *
 * 왜 그냥 RuntimeException을 바로 던지지 않고 커스텀 예외를 만드는가:
 *   - "무슨 문제인지"가 예외 이름 자체로 드러남 (스택 트레이스만 봐도 어떤 종류의 에러인지 파악 가능)
 *   - 나중에 Controller 단에서 @ExceptionHandler로 이 예외를 잡아서
 *     "400 Bad Request + 에러 메시지" 같은 일관된 응답으로 변환하기 쉬워짐
 *   - RuntimeException을 상속했기 때문에 checked exception이 아니라서
 *     메서드 시그니처마다 throws를 안 붙여도 됨 (Spring/JPA 생태계의 일반적인 관례)
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}