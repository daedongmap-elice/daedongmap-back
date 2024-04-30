package com.daedongmap.daedongmap.review.dto;

import lombok.Getter;

@Getter
public class ReviewCreateDto {

    private Long userId;
    private String kakaoPlaceId;
    private String content;
    private float tasteRating;
    private float hygieneRating;
    private float kindnessRating;
    private float averageRating;

}
