package com.daedongmap.daedongmap.reviewImage.dto;

import com.daedongmap.daedongmap.reviewImage.domain.ReviewImage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewImageDto {

    private Long id;
    private Long userId;
    private Long reviewId;
    private String filePath;

    public ReviewImageDto (ReviewImage reviewImage) {
        this.id = reviewImage.getId();
        this.userId = reviewImage.getUser().getId();
        this.reviewId = reviewImage.getReview().getId();
        this.filePath = reviewImage.getFilePath();
    }

}
