package com.daedongmap.daedongmap.review.repository;

import com.daedongmap.daedongmap.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
