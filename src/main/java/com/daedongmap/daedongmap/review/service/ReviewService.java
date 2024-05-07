package com.daedongmap.daedongmap.review.service;

import com.daedongmap.daedongmap.comment.domain.Comment;
import com.daedongmap.daedongmap.comment.repository.CommentRepository;
import com.daedongmap.daedongmap.common.model.Category;
import com.daedongmap.daedongmap.common.model.Region;
import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.likes.repository.LikeRepository;
import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.place.dto.PlaceBasicInfoDto;
import com.daedongmap.daedongmap.place.dto.PlaceCreateDto;
import com.daedongmap.daedongmap.place.repository.PlaceRepository;
import com.daedongmap.daedongmap.place.service.PlaceService;
import com.daedongmap.daedongmap.review.dto.*;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import com.daedongmap.daedongmap.reviewImage.domain.ReviewImage;
import com.daedongmap.daedongmap.reviewImage.repository.ReviewImageRepository;
import com.daedongmap.daedongmap.reviewImage.service.ReviewImageService;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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
    private final PlaceService placeService;

    @Transactional
    public ReviewDto createReview(Long userId, List<MultipartFile> multipartFileList, ReviewCreateDto reviewCreateDto, PlaceCreateDto placeCreateDto) throws IOException {
        Users user = getUserById(userId);

        // 존재하지 않는 경우 : 새로운 장소를 만들어서 반환
        // 존재하는 경우 : 기존 장소를 찾아서 반환
        Optional<Place> place = getOrCreatePlace(placeCreateDto);

        Review review = Review.builder()
                .user(user)
                .place(place.get())
                .content(reviewCreateDto.getContent())
                .hygieneRating(reviewCreateDto.getHygieneRating())
                .tasteRating(reviewCreateDto.getTasteRating())
                .kindnessRating(reviewCreateDto.getKindnessRating())
                .averageRating(reviewCreateDto.getAverageRating())
                .build();

        Review createdReview = reviewRepository.save(review);

        List<ReviewImage> reviewImageList = saveReviewImages(user, review, multipartFileList);
        createdReview.setReviewImageList(reviewImageList);

        return new ReviewDto(createdReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> findAllReviews() {
       List<Review> reviewList = reviewRepository.findAll();
       return reviewList.stream()
               .map(ReviewDto::new)
               .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewDetailDto> findAllReviewByRegionAndCategory(Optional<Region> region, Optional<Category> category, String sort) {
        String regionValue = region.isPresent() ? region.get().getValue() : Region.GANGNAM.getValue();
        String categoryValue = category.isPresent() ? category.get().getValue() : Category.KOREAN.getValue();

        log.info("지역과 카테고리로 리뷰 전체 조회 (service) - " + regionValue + ", " + categoryValue);

        List<Review> reviewList = reviewRepository.findAllByPlaceAddressNameContainingAndPlaceCategoryName(regionValue, categoryValue);
        List<ReviewDetailDto> reviewDtoList = reviewList.stream()
                .map(ReviewDetailDto::new)
                .collect(Collectors.toList());

        if ("DESC".equals(sort)) {
            reviewDtoList.sort(Comparator.comparing(ReviewDetailDto::getCreatedAt).reversed());
        } else if ("POPULAR".equals(sort)) {
            reviewDtoList.sort(Comparator.comparingLong(ReviewDetailDto::getLikeCount));
        }

        return reviewDtoList;
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
    public ReviewDetailDto findReviewById(Long reviewId, Long userId) {
        Review review = getReviewById(reviewId);
        ReviewDetailDto reviewDetailDto = new ReviewDetailDto(review);
        reviewDetailDto.setReviewImageDtoList(reviewImageService.getReviewImage(reviewId));
        reviewDetailDto.setLikeCount(likeRepository.countByReviewId(reviewId));
        Boolean isLikedByUser = likeRepository.existsByReviewAndUser(getReviewById(reviewId), getUserById(userId));
        reviewDetailDto.setIsLikedByUser(isLikedByUser);

        return reviewDetailDto;
    }

    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        Review review =  getReviewById(reviewId);

        if (isReviewOwner(userId, review)) {
            // 리뷰에 해당되는 댓글 삭제
            List<Comment> comments = commentRepository.findAllByReviewId(reviewId);
            for (Comment comment : comments) {
                commentRepository.deleteById(comment.getId());
            }

            // 리뷰에 해당되는 이미지 파일 삭제
            reviewImageService.deleteReviewImage(reviewId);

            // 리뷰 삭제
            reviewRepository.deleteById(reviewId);
        } else {
            throw new CustomException(ErrorCode.REVIEW_NOT_MINE);
        }

    }

    @Transactional
    public ReviewDto updateReview(Long userId, Long reviewId, ReviewUpdateDto reviewUpdateDto, List<MultipartFile> multipartFileList) throws IOException {
        Review review = getReviewById(reviewId);

        if (isReviewOwner(userId, review)) {
            review.updateReview(reviewUpdateDto);

            if (reviewUpdateDto.getImageModified()) {
                log.info("updateReview - 수정된 이미지가 있습니다");
                List<ReviewImage> updateImages = updateReviewImages(review, multipartFileList);
                review.setReviewImageList(updateImages);
            }

            return new ReviewDto(review);
        } else {
            throw new CustomException(ErrorCode.REVIEW_NOT_MINE);
        }
    }

    private List<ReviewImage> updateReviewImages(Review review, List<MultipartFile> multipartFileList) throws IOException {
        reviewImageService.deleteReviewImage(review.getId());
        return saveReviewImages(review.getUser(), review, multipartFileList);
    }

    @Transactional(readOnly = true)
    public String findReviewByKakaoPlaceIdDesc(Long kakaoPlaceId) {
        Review review = reviewRepository.findFirstByKakaoPlaceIdOrderByCreatedAtDesc(kakaoPlaceId);
        List<ReviewImage> reviewImage = reviewImageRepository.findAllByReviewId(review.getId());
        String reviewImagePath = "";

        if (!reviewImage.isEmpty()) {
            reviewImagePath = reviewImage.get(0).getFilePath();
        }
        return reviewImagePath;
    }

    private Users getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Review getReviewById(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        return optionalReview.orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }

    private Optional<Place> getOrCreatePlace(PlaceCreateDto placeCreateDto) {
        Optional<Place> place = placeRepository.findByKakaoPlaceId(placeCreateDto.getKakaoPlaceId());
        if (place.isEmpty()) {
            PlaceBasicInfoDto placeBasicInfoDto = placeService.createPlace(placeCreateDto);
            place = placeRepository.findByKakaoPlaceId(placeBasicInfoDto.getKakaoPlaceId());
        }

        return place;
    }

    private boolean isReviewOwner(Long userId, Review review) {
        return review.getUser().getId().equals(userId);
    }

    private List<ReviewImage> saveReviewImages(Users user, Review review, List<MultipartFile> multipartFileList) throws IOException {
        List<ReviewImage> reviewImages = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFileList) {
            String fileName = multipartFile.getOriginalFilename() + "_" + UUID.randomUUID();
            String filePath = reviewImageService.uploadReviewImage(multipartFile, fileName);

            ReviewImage reviewImage = ReviewImage.builder()
                    .user(user)
                    .review(review)
                    .fileName(fileName)
                    .filePath(filePath)
                    .build();

            reviewImages.add(reviewImageRepository.save(reviewImage));
        }

        return reviewImages;
    }

}
