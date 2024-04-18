package com.daedongmap.daedongmap.review.api;

import com.daedongmap.daedongmap.review.entity.Review;
import com.daedongmap.daedongmap.review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ReviewApiController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/api/reviews")
    public ResponseEntity<Review> createReview() {
        Review createdReview = reviewService.create();

    }

}
