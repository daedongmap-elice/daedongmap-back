package com.daedongmap.daedongmap.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* User */
    EMAIL_IN_USE(HttpStatus.CONFLICT, "사용중인 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "틀린 비밀번호입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

    /* Review */
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 리뷰입니다."),

    /* Comment */
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 댓글입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
