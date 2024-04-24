package com.daedongmap.daedongmap.review.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.place.repository.PlaceRepository;
import com.daedongmap.daedongmap.review.dto.ReviewBasicInfoDto;
import com.daedongmap.daedongmap.review.dto.ReviewCreateDto;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.dto.ReviewUpdateDto;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;


    // todo
    // 1. 리뷰를 작성하고자하는 장소가 데이터에 있는 경우 -> 장소 찾아서 필드에 넣기
    // 2. 리뷰를 작성하고자하는 장소가 데이터에 없는 경우 -> 장소 등록 후 1번 방식
    @Transactional
    public ReviewBasicInfoDto createReview(ReviewCreateDto reviewCreateDto) {
        Users user = userRepository.findById(reviewCreateDto.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Place place = placeRepository.findById(reviewCreateDto.getPlaceId()).orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        Review review = Review.builder()
                .user(user)
                .place(place)
                .title(reviewCreateDto.getTitle())
                .content(reviewCreateDto.getContent())
                .hygieneRating(reviewCreateDto.getHygieneRating())
                .tasteRating(reviewCreateDto.getTasteRating())
                .kindnessRating(reviewCreateDto.getKindnessRating())
                .averageRating(reviewCreateDto.getAverageRating())
                .build();

        Review createdReview = reviewRepository.save(review);
        return new ReviewBasicInfoDto(createdReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewBasicInfoDto> findReviewsByUser(Long userId) {
        List<Review> reviews = reviewRepository.findAllByUserId(userId);
        return reviews.stream()
                .map(ReviewBasicInfoDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewBasicInfoDto> findReviewsByPlace(Long placeId) {
        List<Review> reviews = reviewRepository.findAllByPlaceId(placeId);
        return reviews.stream()
                .map(ReviewBasicInfoDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewBasicInfoDto findReviewById(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review review = optionalReview.orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        return new ReviewBasicInfoDto(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public ReviewBasicInfoDto updateReview(Long reviewId, ReviewUpdateDto reviewUpdateDto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        review.updateReview(reviewUpdateDto);
        return new ReviewBasicInfoDto(review);
    }

}
