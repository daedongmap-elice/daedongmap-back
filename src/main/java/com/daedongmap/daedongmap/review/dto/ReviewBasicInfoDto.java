package com.daedongmap.daedongmap.review.dto;

import com.daedongmap.daedongmap.user.dto.UserBasicInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReviewBasicInfoDto {
    private Long id;
    private Long placeId;
    private UserBasicInfoDto user;
    private String title;
    private String content;
    private float tasteRating;
    private float hygieneRating;
    private float kindnessRating;
    private float averageRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
