package com.daedongmap.daedongmap.reviewImage.repository;

import com.daedongmap.daedongmap.reviewImage.model.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    public List<ReviewImage> findAllByReviewId(Long reviewId);
}
