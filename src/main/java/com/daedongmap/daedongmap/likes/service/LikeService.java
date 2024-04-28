package com.daedongmap.daedongmap.likes.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.likes.domain.Likes;
import com.daedongmap.daedongmap.likes.repository.LikeRepository;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void likeReview(Long userId, Long reviewId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        Likes existingLike = likeRepository.findByUserAndReview(user, review);
        if (existingLike != null) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }

        if (!checkUser(user, review)) {
            throw new CustomException(ErrorCode.LIKE_NOT_ALLOWED_OWN_REVIEW);
        }

        likeRepository.save(new Likes(user, review));
    }

    @Transactional
    public void unlikeReview(Long userId, Long reviewId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        Likes likes = likeRepository.findByUserAndReview(user, review);

        likeRepository.delete(likes);
    }

    private boolean checkUser(Users user, Review review) {
        return !user.getId().equals(review.getUser().getId());
    }

}
