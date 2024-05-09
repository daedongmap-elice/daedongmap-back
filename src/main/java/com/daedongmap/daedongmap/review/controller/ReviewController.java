package com.daedongmap.daedongmap.review.controller;

import com.daedongmap.daedongmap.place.dto.PlaceCreateDto;
import com.daedongmap.daedongmap.place.service.PlaceService;
import com.daedongmap.daedongmap.review.dto.*;
import com.daedongmap.daedongmap.review.service.ReviewService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final PlaceService placeService;

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
        placeService.updatePlaceRate(placeCreateDto.getKakaoPlaceId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReviewDto);
    }

    @GetMapping("/api/reviews")
    @Operation(summary = "리뷰 전체 조회", description = "리뷰 전체를 조회합니다.")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        log.info("리뷰 전체 조회 api 호출");
        List<ReviewDto> reviewDtoList = reviewService.findAllReviews();
        return ResponseEntity.status(HttpStatus.OK).body(reviewDtoList);
    }

    @GetMapping("/api/reviews/filter")
    @Operation(summary = "필터를 적용한 리뷰 전체 조회", description = "필터를 적용해서 리뷰 전체를 조회합니다. 필터는 지역, 음식 종류, 정렬(인기순, 최신순)이 있습니다.")
    public ResponseEntity<List<ReviewDetailDto>> getReviewsByFilter(@RequestParam String region,
                                                                   @RequestParam String category,
                                                                   @RequestParam String sort) {
        log.info("필터를 적용한 리뷰 전체 조회 api 호출 - " + region + ", " + category + ", " + sort);
        List<ReviewDetailDto> reviewDetailDtoList = reviewService.getReviewsByFilter(region, category, sort);

        return ResponseEntity.status(HttpStatus.OK).body(reviewDetailDtoList);
    }

    @GetMapping("/api/reviews/users/{userId}")
    @Operation(summary = "사용자별 리뷰 조회", description = "사용자별 리뷰를 모두 조회합니다.")
    public ResponseEntity<List<ReviewDto>> getReviewsByUser(@PathVariable Long userId) {
        log.info("사용자별 리뷰 조회 api 호출");
        List<ReviewDto> findReviewDtoList = reviewService.findReviewsByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(findReviewDtoList);
    }

    @GetMapping("/api/reviews/places/{kakaoPlaceId}")
    @Operation(summary = "음식점별 리뷰 조회", description = "음식점별 리뷰를 모두 조회합니다.")
    public ResponseEntity<List<ReviewDto>> getReviewsByKakaoPlaceId(@PathVariable Long kakaoPlaceId) {
        log.info("음식점별 리뷰 조회 api 호출 - kakaoPlaceId : " + kakaoPlaceId);
        List<ReviewDto> findReviewDtoList = reviewService.findReviewsByKakaoPlaceId(kakaoPlaceId);
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
    public ResponseEntity<ReviewDetailDto> getReviewById(@PathVariable Long reviewId, @AuthenticationPrincipal CustomUserDetails tokenUser) {
        log.info("리뷰 상세 조회 api 호출 - reviewId : " + reviewId);
        Long userId = tokenUser.getUser().getId();
        ReviewDetailDto reviewDetailDto = reviewService.findReviewById(reviewId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(reviewDetailDto);
    }

    @PutMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    public ResponseEntity<ReviewDto> modifyReview(@PathVariable Long reviewId,
                                                  @RequestPart(value="reviewUpdateRequest") ReviewUpdateDto reviewUpdateDto,
                                                  @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList,
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
