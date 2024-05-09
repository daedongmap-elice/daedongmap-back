package com.daedongmap.daedongmap.review.dto;

import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.reviewImage.dto.ReviewImageDto;
import com.daedongmap.daedongmap.user.dto.UserBasicInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ReviewDto {

    private Long id;
    private Long kakaoPlaceId;
    private String placeName;
    private String content;
    private float tasteRating;
    private float hygieneRating;
    private float kindnessRating;
    private float averageRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserBasicInfoDto user;
    private List<ReviewImageDto> reviewImageDtoList;

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.kakaoPlaceId = review.getPlace().getKakaoPlaceId();
        this.placeName = review.getPlace().getPlaceName();
        this.user = new UserBasicInfoDto(review.getUser());
        this.content = review.getContent();
        this.reviewImageDtoList = review.getReviewImageList().stream()
                .map(ReviewImageDto::new)
                .collect(Collectors.toList());
        this.hygieneRating = review.getHygieneRating();
        this.tasteRating = review.getTasteRating();
        this.kindnessRating = review.getKindnessRating();
        this.averageRating = review.getAverageRating();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }

}
