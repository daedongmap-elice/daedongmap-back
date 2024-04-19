package com.daedongmap.daedongmap.review.dto;

import lombok.Getter;

@Getter
public class ReviewCreateDto {

    private Long userId;
    private Long placeId;
    private String title;
    private String content;
    private float rating;

}
