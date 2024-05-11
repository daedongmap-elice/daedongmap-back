package com.daedongmap.daedongmap.likes.repository;

import com.daedongmap.daedongmap.likes.domain.Likes;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Likes findByUserAndReview(Users user, Review review);
    Boolean existsByReviewAndUser(Review review, Users user);
    Long countByReviewId(Long reviewId);
}
