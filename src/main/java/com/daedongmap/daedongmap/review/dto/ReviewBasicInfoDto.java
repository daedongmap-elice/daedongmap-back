package com.daedongmap.daedongmap.review.dto;

import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.user.dto.UserBasicInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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

    public ReviewBasicInfoDto(Review review) {
        this.id = review.getId();
        this.placeId = review.getPlace().getId();
        this.user = new UserBasicInfoDto(review.getUser());
        this.title = review.getTitle();
        this.content = review.getContent();
        this.hygieneRating = review.getHygieneRating();
        this.tasteRating = review.getTasteRating();
        this.kindnessRating = review.getKindnessRating();
        this.averageRating = review.getAverageRating();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }

}
