package com.daedongmap.daedongmap.reviewImage.repository;

import com.daedongmap.daedongmap.reviewImage.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findAllByReviewId(Long reviewId);

    void deleteByReviewId(Long reviewId);
}
