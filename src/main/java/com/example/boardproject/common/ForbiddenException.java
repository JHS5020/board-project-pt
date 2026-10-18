package com.example.boardproject.common;

/** 로그인은 했지만 해당 작업을 할 권한이 없을 때 (403) */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}