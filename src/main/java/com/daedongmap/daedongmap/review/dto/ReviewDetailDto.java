package com.daedongmap.daedongmap.review.dto;

import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.reviewImage.dto.ReviewImageDto;
import com.daedongmap.daedongmap.user.dto.UserBasicInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReviewDetailDto {

    private Long id;
    private Long kakaoPlaceId;
    private UserBasicInfoDto user;
    private String content;
    private List<ReviewImageDto> reviewImageDtoList = new ArrayList<>();
    private float tasteRating;
    private float hygieneRating;
    private float kindnessRating;
    private float averageRating;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewDetailDto(Review review) {
        this.id = review.getId();
        this.kakaoPlaceId = review.getPlace().getKakaoPlaceId();
        this.user = new UserBasicInfoDto(review.getUser());
        this.content = review.getContent();
        this.hygieneRating = review.getHygieneRating();
        this.tasteRating = review.getTasteRating();
        this.kindnessRating = review.getKindnessRating();
        this.averageRating = review.getAverageRating();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }

}
