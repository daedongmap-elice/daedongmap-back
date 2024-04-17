package com.daedongmap.daedongmap.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EMAIL_IN_USE(HttpStatus.CONFLICT, "사용중인 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "틀린 비밀번호입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
