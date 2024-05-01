package com.daedongmap.daedongmap.review.controller;

import com.daedongmap.daedongmap.place.dto.PlaceCreateDto;
import com.daedongmap.daedongmap.place.service.PlaceService;
import com.daedongmap.daedongmap.review.dto.*;
import com.daedongmap.daedongmap.review.domain.Review;
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
    private final PlaceService placeService;

    @PostMapping("/api/reviews")
    @Operation(summary = "리뷰 작성", description = "리뷰를 작성합니다.")
    public ResponseEntity<ReviewDto> createReview(@RequestPart(value="file") List<MultipartFile> multipartFileList,
                                                  @RequestPart(value="reviewRequest") ReviewCreateDto reviewCreateDto,
                                                  @RequestPart(value="placeRequest")PlaceCreateDto placeCreateDto) throws IOException {
        ReviewDto createdReviewDto = reviewService.createReview(multipartFileList, reviewCreateDto, placeCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReviewDto);
    }

    @GetMapping("/api/reviews")
    @Operation(summary = "리뷰 전체 조회", description = "갤러리 형식으로 리뷰 전체를 조회합니다. 리뷰 게시물의 첫 번째 사진과 id를 반환합니다.")
    public ResponseEntity<List<ReviewGalleryDto>> getReviewGallery(@RequestParam(defaultValue = "recommended") String type,
                                                                   @RequestParam(defaultValue = "nationwide") String region) {
        return null;
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
