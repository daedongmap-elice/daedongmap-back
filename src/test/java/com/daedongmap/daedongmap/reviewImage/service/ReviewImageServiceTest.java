package com.daedongmap.daedongmap.reviewImage.service;

import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import com.daedongmap.daedongmap.reviewImage.domain.ReviewImage;
import com.daedongmap.daedongmap.reviewImage.dto.ReviewImageDto;
import com.daedongmap.daedongmap.reviewImage.repository.ReviewImageRepository;
import com.daedongmap.daedongmap.user.domain.Authority;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewImageServiceTest {

    @Mock
    private ReviewImageRepository reviewImageRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ReviewImageService reviewImageService;

    @Test
    @DisplayName("리뷰 이미지 가져오기 (성공)")
    void getReviewImage_success() {
        // given
        Users mockUser = Users.builder()
                .id(1L)
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();
        Review mockReview = Review.builder().id(1L).build();
        List<ReviewImage> mockReviewImageList = new ArrayList<>();
        mockReviewImageList.add(ReviewImage.builder()
                        .id(1L)
                        .review(mockReview)
                        .user(mockUser)
                        .fileName("mockReviewImage1.jpg")
                        .filePath("27g38fhe8_mockReviewImage1.jpg")
                        .build());
        mockReviewImageList.add(ReviewImage.builder()
                .id(2L)
                .review(mockReview)
                .user(mockUser)
                .fileName("mockReviewImage2.jpg")
                .filePath("27g38fhe8_mockReviewImage2.jpg")
                .build());

        lenient().when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        lenient().when(reviewRepository.findById(mockReview.getId())).thenReturn(Optional.of(mockReview));
        lenient().when(reviewImageRepository.findAllByReviewId(mockReview.getId())).thenReturn(mockReviewImageList);

        // when
        List<ReviewImageDto> result = reviewImageService.getReviewImage(mockReview.getId());

        // then
        assertEquals(mockReviewImageList.size(), result.size());
        for (int i = 0; i < mockReviewImageList.size(); i++) {
            assertEquals(mockReviewImageList.get(i).getFilePath(), result.get(i).getFilePath());
        }
    }

}