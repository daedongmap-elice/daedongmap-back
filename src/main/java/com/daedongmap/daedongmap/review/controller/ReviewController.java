package com.daedongmap.daedongmap.review.controller;

import com.daedongmap.daedongmap.review.dto.ReviewCreateDto;
import com.daedongmap.daedongmap.reviewImage.model.ReviewImage;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.dto.ReviewBasicInfoDto;
import com.daedongmap.daedongmap.review.dto.ReviewUpdateDto;
import com.daedongmap.daedongmap.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/api/reviews")
    @Operation(summary = "리뷰 작성", description = "리뷰를 작성합니다.")
    public ResponseEntity<ReviewBasicInfoDto> createReview(@RequestPart(value="file") List<MultipartFile> multipartFileList,
                                                           @RequestPart(value="request") ReviewCreateDto reviewCreateDto) throws IOException {
        ReviewBasicInfoDto createdReviewDto = reviewService.createReview(multipartFileList, reviewCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReviewDto);
    }

    @GetMapping("/api/reviews/users/{userId}")
    @Operation(summary = "사용자별 리뷰 조회", description = "사용자별 리뷰를 모두 조회합니다.")
    public ResponseEntity<List<ReviewBasicInfoDto>> getReviewsByUser(@PathVariable Long userId) {
        List<ReviewBasicInfoDto> findReviewDtos = reviewService.findReviewsByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(findReviewDtos);
    }

    @GetMapping("/api/reviews/places/{placeId}")
    @Operation(summary = "음식점별 리뷰 조회", description = "음식점별 리뷰를 모두 조회합니다.")
    public ResponseEntity<List<ReviewBasicInfoDto>> getReviewsByPlace(@PathVariable Long placeId) {
        List<ReviewBasicInfoDto> findReviewDtos = reviewService.findReviewsByPlace(placeId);
        return ResponseEntity.status(HttpStatus.OK).body(findReviewDtos);
    }

    @GetMapping("/api/reviews/users/me")
    @Operation(summary = "내 리뷰 조회", description = "내가 작성한 리뷰를 모두 조회합니다.")
    public ResponseEntity<List<Review>> getReviewsByMe() {
        return null;
    }

    @GetMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 상세 조회", description = "리뷰를 상세조회합니다.")
    public ResponseEntity<ReviewBasicInfoDto> getReviewById(@PathVariable Long reviewId) {
        ReviewBasicInfoDto findReviewDto = reviewService.findReviewById(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(findReviewDto);
    }

    @PutMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    public ResponseEntity<ReviewBasicInfoDto> modifyReview(@PathVariable Long reviewId, @RequestBody ReviewUpdateDto reviewUpdateDto) {
        ReviewBasicInfoDto updatedReviewDto = reviewService.updateReview(reviewId, reviewUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedReviewDto);
    }

    @DeleteMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }


}
