package com.daedongmap.daedongmap.review.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.review.dto.ReviewBasicInfoDto;
import com.daedongmap.daedongmap.review.dto.ReviewCreateDto;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.dto.ReviewUpdateDto;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.UserBasicInfoDto;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;


    @Transactional
    public ReviewBasicInfoDto createReview(ReviewCreateDto reviewCreateDto) {
        Users user = userRepository.findById(reviewCreateDto.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Review review = Review.builder()
                .user(user)
                .title(reviewCreateDto.getTitle())
                .content(reviewCreateDto.getContent())
                .rating(reviewCreateDto.getRating())
                .build();

        Review createdReview = reviewRepository.save(review);
        return toReviewBasicInfoDto(createdReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewBasicInfoDto> findReviewsByUser(Long userId) {
        List<Review> reviews = reviewRepository.findAllByUserId(userId);
        return reviews.stream()
                .map(this::toReviewBasicInfoDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewBasicInfoDto findReviewById(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review review = optionalReview.orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        return toReviewBasicInfoDto(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public ReviewBasicInfoDto updateReview(Long reviewId, ReviewUpdateDto reviewUpdateDto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        review.updateReview(reviewUpdateDto);
        return toReviewBasicInfoDto(review);
    }

    private ReviewBasicInfoDto toReviewBasicInfoDto(Review review) {
        return ReviewBasicInfoDto.builder()
                .id(review.getId())
                .user(UserBasicInfoDto.builder()
                        .id(review.getUser().getId())
                        .nickName(review.getUser().getNickName())
                        .email(review.getUser().getEmail())
                        .build())
                .title(review.getTitle())
                .content(review.getContent())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
