package com.example.boardproject.common;

/** 요청한 리소스가 없을 때(또는 삭제됐을 때) 던지는 예외 → 404 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}