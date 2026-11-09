package com.example.boardproject.common;

/** 이미 처리된 요청과 충돌할 때 (409). 예: 이미 추천한 글에 다시 추천 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}