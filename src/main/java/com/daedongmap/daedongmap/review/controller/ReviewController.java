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
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final PlaceService placeService;
    private final ReviewImageService reviewImageService;
    private final ReviewImageRepository reviewImageRepository;

    @PostMapping("/api/reviews")
    @Operation(summary = "리뷰 작성", description = "리뷰를 작성합니다.")
    public ResponseEntity<ReviewDto> createReview(@RequestPart(value="file", required=false) List<MultipartFile> multipartFileList,
                                                  @RequestPart(value="reviewRequest") ReviewCreateDto reviewCreateDto,
                                                  @RequestPart(value="placeRequest") PlaceCreateDto placeCreateDto) throws IOException {
        ReviewDto createdReviewDto = reviewService.createReview(multipartFileList, reviewCreateDto, placeCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReviewDto);
    }

    // 이미지 업로드 확인용 api
    @PostMapping("/api/reviews/test")
    @Operation(summary = "테스트", description = "테스트")
    public ResponseEntity<ReviewImage> reviewTest(@RequestPart(value="file", required=false) MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename() + "_" + UUID.randomUUID();
        String filePath = reviewImageService.uploadReviewImage(multipartFile, fileName);

        ReviewImage reviewImage = ReviewImage.builder()
                .filePath(filePath)
                .fileName(fileName)
                .build();

        reviewImageRepository.save(reviewImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewImage);
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
        log.info("지역과 카테고리로 리뷰 전체 조회 (controller) - " + region + ", " + category);
        List<ReviewDetailDto> reviewDtoList = reviewService.findAllReviewByRegionAndCategory(region, category, sort);
        return ResponseEntity.status(HttpStatus.OK).body(reviewDtoList);
    }

    @GetMapping("/api/reviews/users/{userId}")
    @Operation(summary = "사용자별 리뷰 조회", description = "사용자별 리뷰를 모두 조회합니다.")
    public ResponseEntity<List<ReviewDto>> getReviewsByUser(@PathVariable Long userId) {
        List<ReviewDto> findReviewDtos = reviewService.findReviewsByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(findReviewDtos);
    }

    @GetMapping("/api/reviews/places/{placeId}")
    @Operation(summary = "음식점별 리뷰 조회", description = "음식점별 리뷰를 모두 조회합니다.")
    public ResponseEntity<List<ReviewDto>> getReviewsByPlace(@PathVariable Long placeId) {
        List<ReviewDto> findReviewDtos = reviewService.findReviewsByPlace(placeId);
        return ResponseEntity.status(HttpStatus.OK).body(findReviewDtos);
    }

    // todo: 토큰으로부터 id값 가져와서 내 리뷰만 조회
    @GetMapping("/api/reviews/users/me")
    @Operation(summary = "내 리뷰 조회", description = "내가 작성한 리뷰를 모두 조회합니다.")
    public ResponseEntity<List<Review>> getReviewsByMe() {
        return null;
    }

    @GetMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 상세 조회", description = "리뷰를 상세조회합니다.")
    public ResponseEntity<ReviewDetailDto> getReviewById(@PathVariable Long reviewId) {
        ReviewDetailDto reviewDetailDto = reviewService.findReviewById(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(reviewDetailDto);
    }

    // todo: 본인만 수정 가능, 토큰으로부터 가져온 id와 포스트 작성자의 id가 일치하는지 확인 후 수정
    @PutMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    public ResponseEntity<ReviewDto> modifyReview(@PathVariable Long reviewId, @RequestBody ReviewUpdateDto reviewUpdateDto) {
        ReviewDto updatedReviewDto = reviewService.updateReview(reviewId, reviewUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedReviewDto);
    }

    @DeleteMapping("/api/reviews/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }

}
