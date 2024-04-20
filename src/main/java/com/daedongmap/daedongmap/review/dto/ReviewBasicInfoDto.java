package com.daedongmap.daedongmap.review.dto;

import com.daedongmap.daedongmap.review.domain.Review;
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
    private UserBasicInfoDto user;
    private String title;
    private String content;
    private float rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
