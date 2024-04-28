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

    /* Token */
    UNAUTHORIZED_TOKEN(HttpStatus.FORBIDDEN, "인증되지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.NOT_ACCEPTABLE, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원되지 않는 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    /* Review */
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 리뷰입니다."),

    /* Comment */
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 댓글입니다."),

    /* Like */
    LIKE_NOT_ALLOWED_OWN_REVIEW(HttpStatus.BAD_REQUEST, "내 리뷰에는 좋아요를 누를 수 없습니다."),
    ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 해당 리뷰에 좋아요를 눌렀습니다."),

    /* Place */
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 장소입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
