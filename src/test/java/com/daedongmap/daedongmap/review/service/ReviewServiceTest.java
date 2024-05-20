package com.daedongmap.daedongmap.review.service;

import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.place.dto.PlaceCreateDto;
import com.daedongmap.daedongmap.place.repository.PlaceRepository;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.dto.ReviewCreateDto;
import com.daedongmap.daedongmap.review.dto.ReviewDto;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import com.daedongmap.daedongmap.reviewImage.domain.ReviewImage;
import com.daedongmap.daedongmap.reviewImage.repository.ReviewImageRepository;
import com.daedongmap.daedongmap.reviewImage.service.ReviewImageService;
import com.daedongmap.daedongmap.s3.S3Service;
import com.daedongmap.daedongmap.user.domain.Authority;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewImageRepository reviewImageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PlaceRepository placeRepository;
    @Mock
    private S3Service s3Service;
    @Mock
    private ReviewImageService reviewImageService;
    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("장소 새로 저장하면서 리뷰 작성 (성공)")
    void createReviewWithNewPlace_success() throws IOException {
        // given
        MultipartFile multipartFile = new MockMultipartFile("file", "fileContent".getBytes());
        List<MultipartFile> multipartFileList = new ArrayList<>();
        multipartFileList.add(multipartFile);

        Users user = Users.builder()
                .id(1L)
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        PlaceCreateDto placeCreateDto = PlaceCreateDto.builder()
                .kakaoPlaceId(1L)
                .placeName("testPlace")
                .build();

        ReviewCreateDto reviewCreateDto = ReviewCreateDto.builder()
                .content("testReview")
                .hygieneRating(4.0f)
                .tasteRating(4.5f)
                .kindnessRating(5.0f)
                .averageRating(4.5f)
                .build();

        Place place = Place.builder().kakaoPlaceId(1L).build();

        Review review = Review.builder()
                .id(1L)
                .user(user)
                .place(place)
                .content(reviewCreateDto.getContent())
                .hygieneRating(reviewCreateDto.getHygieneRating())
                .tasteRating(reviewCreateDto.getTasteRating())
                .kindnessRating(reviewCreateDto.getKindnessRating())
                .averageRating(reviewCreateDto.getAverageRating())
                .build();

        ReviewImage reviewImage = ReviewImage.builder()
                .id(1L)
                .user(user)
                .review(review)
                .fileName("file")
                .filePath("filePath")
                .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(placeRepository.findByKakaoPlaceId(1L)).willReturn(Optional.of(place));
        given(s3Service.uploadImage(multipartFile, "review")).willReturn("filePath");
        given(reviewImageRepository.save(any(ReviewImage.class))).willReturn(reviewImage);
        given(reviewRepository.save(any())).willReturn(review);

        // when
        ReviewDto reviewDto = reviewService.createReview(1L, multipartFileList, reviewCreateDto, placeCreateDto);

        // then
        verify(userRepository, times(1)).findById(1L);
        verify(placeRepository, times(1)).findByKakaoPlaceId(1L);
        verify(s3Service, times(1)).uploadImage(multipartFile, "review");
        verify(reviewImageRepository, times(1)).save(any(ReviewImage.class));
        verify(reviewRepository, times(1)).save(any(Review.class));

        assertNotNull(reviewDto);
        assertEquals("testReview", reviewDto.getContent());
        assertEquals(4.5f, reviewDto.getAverageRating());

        assertNotNull(reviewDto.getReviewImageDtoList());
        assertEquals(1, reviewDto.getReviewImageDtoList().size());
        assertEquals("filePath", reviewDto.getReviewImageDtoList().get(0).getFilePath());
    }

}