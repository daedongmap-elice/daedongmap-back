package com.daedongmap.daedongmap.review.controller;

import com.daedongmap.daedongmap.common.model.Category;
import com.daedongmap.daedongmap.common.model.Region;
import com.daedongmap.daedongmap.place.dto.PlaceCreateDto;
import com.daedongmap.daedongmap.place.service.PlaceService;
import com.daedongmap.daedongmap.review.dto.*;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import com.daedongmap.daedongmap.review.service.ReviewService;
import com.daedongmap.daedongmap.reviewImage.domain.ReviewImage;
import com.daedongmap.daedongmap.reviewImage.repository.ReviewImageRepository;
import com.daedongmap.daedongmap.reviewImage.service.ReviewImageService;
import com.daedongmap.daedongmap.security.jwt.TokenProvider;
import com.daedongmap.daedongmap.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/api/reviews")
    @Operation(summary = "리뷰 작성", description = "리뷰를 작성합니다.")
    public ResponseEntity<ReviewDto> createReview(@RequestPart(value="file", required=false) List<MultipartFile> multipartFileList,
                                                  @RequestPart(value="reviewRequest") ReviewCreateDto reviewCreateDto,
                                                  @RequestPart(value="placeRequest") PlaceCreateDto placeCreateDto,
                                                  @AuthenticationPrincipal CustomUserDetails tokenUser) throws IOException {
        log.info("리뷰 작성 api 호출 - multipartFileList : " + multipartFileList + ", reviewRequest : " + reviewCreateDto.toString() + ", placeRequest : " + placeCreateDto.toString());

        Long userId = tokenUser.getUser().getId();
        log.info("리뷰 작성 api 호출 - userId : " + userId);

        ReviewDto createdReviewDto = reviewService.createReview(userId, multipartFileList, reviewCreateDto, placeCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReviewDto);
    }

    @GetMapping("/api/reviews")
    @Operation(summary = "리뷰 전체 조회", description = "리뷰 전체를 조회합니다.")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviewDtoList = reviewService.findAllReviews();
        return ResponseEntity.status(HttpStatus.OK).body(reviewDtoList);
    }

    @GetMapping("/api/reviews/region/{region}/category/{category}")
    @Operation(summary = "지역과 카테고리로 리뷰 전체 조회", description = "지역과 카테고리로 리뷰 전체를 조회합니다.")
    public ResponseEntity<List<ReviewDetailDto>> getAllReviewByRegionAndCategory(@PathVariable(required = false) Optional<Region> region,
                                                                           @PathVariable(required = false) Optional<Category> category,
                                                                           @RequestParam(defaultValue = "DESC") String sort) {
        log.info("지역과 카테고리로 리뷰 전체 조회 api 호출 - " + region + ", " + category);
        List<ReviewDetailDto> reviewDtoList = reviewService.findAllReviewByRegionAndCategory(region, category, sort);
        return ResponseEntity.status(HttpStatus.OK).body(reviewDtoList);
    }

    @GetMapping("/api/reviews/users/{userId}")
    @Operation(summary = "사용자별 리뷰 조회", description = "사용자별 리뷰를 모두 조회합니다.")
    public ResponseEntity<List<ReviewDto>> getReviewsByUser(@PathVariable Long userId) {
        List<ReviewDto> findReviewDtoList = reviewService.findReviewsByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(findReviewDtoList);
    }

    @GetMapping("/api/reviews/places/{placeId}")
    @Operation(summary = "음식점별 리뷰 조회", description = "음식점별 리뷰를 모두 조회합니다.")
    public ResponseEntity<List<ReviewDto>> getReviewsByPlace(@PathVariable Long placeId) {
        List<ReviewDto> findReviewDtoList = reviewService.findReviewsByPlace(placeId);
        return ResponseEntity.status(HttpStatus.OK).body(findReviewDtoList);
    }

    @GetMapping("/api/reviews/users/me")
    @Operation(summary = "내 리뷰 조회", description = "내가 작성한 리뷰를 모두 조회합니다.")
    public ResponseEntity<List<ReviewDto>> getReviewsByMe(@AuthenticationPrincipal CustomUserDetails tokenUser) {
        Long userId = tokenUser.getUser().getId();
        log.info("내 리뷰 조회 api 호출 - userId : " + userId);

        List<ReviewDto> findReviewDtoList = reviewService.findReviewsByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(findReviewDtoList);
    }

    @GetMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 상세 조회", description = "리뷰를 상세조회합니다.")
    public ResponseEntity<ReviewDetailDto> getReviewById(@PathVariable Long reviewId) {
        ReviewDetailDto reviewDetailDto = reviewService.findReviewById(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(reviewDetailDto);
    }

    @PutMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    public ResponseEntity<ReviewDto> modifyReview(@PathVariable Long reviewId,
                                                  @RequestPart(value="reviewUpdateRequest") ReviewUpdateDto reviewUpdateDto,
                                                  @RequestPart(value = "file") List<MultipartFile> multipartFileList,
                                                  @AuthenticationPrincipal CustomUserDetails tokenUser) throws IOException {
        Long userId = tokenUser.getUser().getId();
        log.info("리뷰 수정 api 호출 - userId : " + userId + ", reviewId : " + reviewId);

        ReviewDto updatedReviewDto = reviewService.updateReview(userId, reviewId, reviewUpdateDto, multipartFileList);
        return ResponseEntity.status(HttpStatus.OK).body(updatedReviewDto);
    }

    @DeleteMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    public void deleteReview(@PathVariable Long reviewId,  @AuthenticationPrincipal CustomUserDetails tokenUser) {
        Long userId = tokenUser.getUser().getId();
        log.info("리뷰 삭제 api 호출 - userId : " + userId + ", reviewId : " + reviewId);

        reviewService.deleteReview(userId, reviewId);
    }

}
