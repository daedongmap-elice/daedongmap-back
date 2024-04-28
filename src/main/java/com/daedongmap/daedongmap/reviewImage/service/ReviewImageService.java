package com.daedongmap.daedongmap.reviewImage.service;


import com.daedongmap.daedongmap.reviewImage.dto.ReviewImageDto;
import com.daedongmap.daedongmap.reviewImage.model.ReviewImage;
import com.daedongmap.daedongmap.reviewImage.repository.ReviewImageRepository;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ReviewImageService implements ImageFileService {

    private final ReviewImageRepository reviewImageRepository;

    public ReviewImageService(ReviewImageRepository reviewImageRepository) {
        this.reviewImageRepository = reviewImageRepository;
    }

    public List<ReviewImageDto> getReviewImage(Long reviewId) {
        List<ReviewImage> reviewImageList = reviewImageRepository.findAllByReviewId(reviewId);
        return reviewImageList.stream()
                .map(ReviewImageDto::new)
                .collect(Collectors.toList());
    }

}
