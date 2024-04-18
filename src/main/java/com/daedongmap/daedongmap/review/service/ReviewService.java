package com.daedongmap.daedongmap.review.service;

import com.daedongmap.daedongmap.review.entity.Review;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review createReview() {
        Review review
    }
}
