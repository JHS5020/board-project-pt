package com.example.boardproject.common;

/** 로그인이 필요한 리소스에 비로그인 상태로 접근했을 때 던지는 예외 → 401 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}