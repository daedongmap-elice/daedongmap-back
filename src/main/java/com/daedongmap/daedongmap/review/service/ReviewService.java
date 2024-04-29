package com.daedongmap.daedongmap.review.service;

import com.daedongmap.daedongmap.comment.domain.Comment;
import com.daedongmap.daedongmap.comment.repository.CommentRepository;
import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.likes.repository.LikeRepository;
import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.place.repository.PlaceRepository;
import com.daedongmap.daedongmap.review.dto.*;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import com.daedongmap.daedongmap.reviewImage.model.ReviewImage;
import com.daedongmap.daedongmap.reviewImage.repository.ReviewImageRepository;
import com.daedongmap.daedongmap.reviewImage.service.ReviewImageService;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final ReviewImageService reviewImageService;

    // todo
    // 1. 리뷰를 작성하고자하는 장소가 데이터에 있는 경우 -> 장소 찾아서 필드에 넣기
    // 2. 리뷰를 작성하고자하는 장소가 데이터에 없는 경우 -> 장소 등록 후 1번 방식
    @Transactional
    public ReviewDto createReview(List<MultipartFile> multipartFileList, ReviewCreateDto reviewCreateDto) throws IOException {
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

        for (MultipartFile multipartFile : multipartFileList) {
            // 이미지를 저장하고 파일 경로를 반환
            String fileName = multipartFile.getOriginalFilename() + "_" + UUID.randomUUID();
            String filePath = reviewImageService.uploadReviewImage(multipartFile, fileName);

            ReviewImage reviewImage = ReviewImage.builder()
                    .user(user)
                    .review(createdReview)
                    .filePath(filePath)
                    .fileName(fileName)
                    .build();

            reviewImageRepository.save(reviewImage);
        }

        return new ReviewDto(createdReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewGalleryDto> findReviewsByTypeAndRegion(String type, String region) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> findReviewsByUser(Long userId) {
        List<Review> reviews = reviewRepository.findAllByUserId(userId);
        return reviews.stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> findReviewsByPlace(Long placeId) {
        List<Review> reviews = reviewRepository.findAllByPlaceId(placeId);
        return reviews.stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewDetailDto findReviewById(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review review = optionalReview.orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        ReviewDetailDto reviewDetailDto = new ReviewDetailDto(review);
        reviewDetailDto.setReviewImageDtoList(reviewImageService.getReviewImage(reviewId));
        reviewDetailDto.setLikeCount(likeRepository.countByReviewId(reviewId));

        return reviewDetailDto;
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        // 리뷰에 해당되는 댓글 삭제
        List<Comment> comments = commentRepository.findAllByReviewId(reviewId);
        for (Comment comment : comments) {
            commentRepository.deleteById(comment.getId());
        }

        // 리뷰에 해당되는 이미지 파일 삭제
        reviewImageService.deleteReviewImage(reviewId);

        // 리뷰 삭제
        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public ReviewDto updateReview(Long reviewId, ReviewUpdateDto reviewUpdateDto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        review.updateReview(reviewUpdateDto);
        return new ReviewDto(review);
    }

}
