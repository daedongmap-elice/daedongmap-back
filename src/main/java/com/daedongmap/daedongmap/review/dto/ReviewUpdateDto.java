package com.daedongmap.daedongmap.review.dto;

import lombok.Getter;

@Getter
public class ReviewUpdateDto {

    private String title;
    private String content;
    private float tasteRating;
    private float hygieneRating;
    private float kindnessRating;
    private float averageRating;

}
