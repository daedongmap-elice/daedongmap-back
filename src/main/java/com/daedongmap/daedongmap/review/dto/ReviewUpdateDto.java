package com.daedongmap.daedongmap.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewUpdateDto {

    private String content;
    private float tasteRating;
    private float hygieneRating;
    private float kindnessRating;
    private float averageRating;
    private Boolean imageModified;

}
