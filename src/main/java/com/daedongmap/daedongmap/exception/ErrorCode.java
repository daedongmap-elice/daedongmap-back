package com.daedongmap.daedongmap.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* User */
    EMAIL_IN_USE(HttpStatus.CONFLICT, "사용중인 이메일입니다."),
    PHONE_IN_USE(HttpStatus.CONFLICT, "이미 등록된 휴대폰 번호입니다. 아이디 찾기를 사용해주세요."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "틀린 비밀번호입니다."),
    OAUTH_USER(HttpStatus.UNAUTHORIZED, "간편 로그인 사용자입니다."),

    /* Token */
    UNAUTHORIZED_TOKEN(HttpStatus.FORBIDDEN, "인증되지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.NOT_ACCEPTABLE, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원되지 않는 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "다시 로그인 해주세요."),
    TOKEN_ERROR(HttpStatus.CONFLICT, "토큰 발급 중 문제가 생겼습니다."),

    /* Review */
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 리뷰입니다."),
    REVIEW_NOT_MINE(HttpStatus.NOT_FOUND, "본인이 등록한 리뷰가 아닙니다."),

    /* Comment */
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 댓글입니다."),

    /* Like */
    LIKE_NOT_ALLOWED_OWN_REVIEW(HttpStatus.BAD_REQUEST, "내 리뷰에는 좋아요를 누를 수 없습니다."),
    ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 해당 리뷰에 좋아요를 눌렀습니다."),

    /* Follow */
    FOLLOW_MYSELF_NOW_ALLOWED(HttpStatus.BAD_REQUEST, "본인에게는 팔로우할 수 없습니다."),
    FOLLOW_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 팔로우했습니다."),

    /* Place */
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 장소입니다."),
    PLACE_KAKAO_ID_IN_USE(HttpStatus.CONFLICT, "등록된 장소입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
