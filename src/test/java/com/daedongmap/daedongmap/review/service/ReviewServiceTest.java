package com.daedongmap.daedongmap.review.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.place.dto.PlaceBasicInfoDto;
import com.daedongmap.daedongmap.place.dto.PlaceCreateDto;
import com.daedongmap.daedongmap.place.repository.PlaceRepository;
import com.daedongmap.daedongmap.place.service.PlaceService;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.dto.ReviewCreateDto;
import com.daedongmap.daedongmap.review.dto.ReviewDetailDto;
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
import static org.mockito.Mockito.*;

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
    @Mock
    private PlaceService placeService;
    @InjectMocks
    private ReviewService reviewService;

    // todo : 장소가 있을 때 없을 때 둘 다 테스트해야함
    @Test
    @DisplayName("리뷰 작성 (성공)")
    void createReview_success() throws IOException {
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
        assertEquals(1L, reviewDto.getKakaoPlaceId());

        assertNotNull(reviewDto.getReviewImageDtoList());
        assertEquals(1, reviewDto.getReviewImageDtoList().size());
        assertEquals("filePath", reviewDto.getReviewImageDtoList().get(0).getFilePath());
    }

    @Test
    @DisplayName("모든 리뷰 조회 (성공)")
    void getAllReviews_success() {
        // given
        Place mockPlace = Place.builder()
                .kakaoPlaceId(1L)
                .build();

        Users mockUser = Users.builder()
                .id(1L)
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        List<Review> mockReviewList = new ArrayList<>();
        mockReviewList.add(Review.builder()
                .id(1L)
                .place(mockPlace)
                .user(mockUser)
                .build());

        // when
        when(reviewRepository.findAll()).thenReturn(mockReviewList);
        List<ReviewDto> reviewDtoList = reviewService.findAllReviews();

        // then
        verify(reviewRepository, times(1)).findAll();
        assertEquals(mockReviewList.size(), reviewDtoList.size());
    }

    @Test
    @DisplayName("모든 리뷰 조회 (실패)")
    void getAllReviews_failure() {
        // given
        Place mockPlace = Place.builder()
                .kakaoPlaceId(1L)
                .build();

        Users mockUser = Users.builder()
                .id(1L)
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        List<Review> mockReviewList = new ArrayList<>();
        mockReviewList.add(Review.builder()
                .id(1L)
                .place(mockPlace)
                .user(mockUser)
                .build());

        // when
        when(reviewRepository.findAll()).thenReturn(Collections.emptyList());
        List<ReviewDto> reviewDtoList = reviewService.findAllReviews();

        // then
        verify(reviewRepository, times(1)).findAll();
        assertNotEquals(mockReviewList.size(), reviewDtoList.size(), "기대했던 사이즈와 다릅니다.");
    }

    @Test
    @DisplayName("사용자별 작성한 리뷰 조회 (성공)")
    void getReviewsByUser_success() {
        // given
        Long userId = 1L;
        Place mockPlace = Place.builder()
                .kakaoPlaceId(1L)
                .placeName("mockPlace")
                .build();

        Users mockUser = Users.builder()
                .id(userId)
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        List<Review> mockReviewList = new ArrayList<>();
        mockReviewList.add(Review.builder()
                .id(1L)
                .place(mockPlace)
                .user(mockUser)
                .content("Great place!")
                .build());

        // when
        when(reviewRepository.findAllByUserId(userId)).thenReturn(mockReviewList);
        List<ReviewDetailDto> reviewDetailDtoList = reviewService.findReviewsByUser(userId);

        // then
        verify(reviewRepository, times(1)).findAllByUserId(userId);
        assertEquals(mockReviewList.size(), reviewDetailDtoList.size());

        for (int i = 0; i < mockReviewList.size(); i++) {
            Review review = mockReviewList.get(i);
            ReviewDetailDto reviewDetailDto = reviewDetailDtoList.get(i);

            assertEquals(review.getId(), reviewDetailDto.getId());
            assertEquals(review.getPlace().getKakaoPlaceId(), reviewDetailDto.getKakaoPlaceId());
            assertEquals(review.getUser().getId(), reviewDetailDto.getUser().getId());
        }
    }

    @Test
    @DisplayName("사용자별 작성한 리뷰 조회 (실패 - 존재하지 않는 사용자 ID)")
    void getReviewsByUser_failure() {
        // given
        Long userId = 1L;

        // when
        when(reviewRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());
        List<ReviewDetailDto> reviewDetailDtoList = reviewService.findReviewsByUser(userId);

        // then
        verify(reviewRepository, times(1)).findAllByUserId(userId);
        assertTrue(reviewDetailDtoList.isEmpty());
    }

    @Test
    @DisplayName("음식점별 리뷰 조회 (성공)")
    void getReviewsByKakaoPlace_success() {
        // given
        Users mockUser = Users.builder()
                .id(1L)
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        Long kakaoPlaceId = 1L;
        Place mockPlace = Place.builder()
                .kakaoPlaceId(kakaoPlaceId)
                .placeName("mockPlace")
                .build();

        List<Review> mockReviewList = new ArrayList<>();
        mockReviewList.add(Review.builder()
                .id(1L)
                .place(mockPlace)
                .user(mockUser)
                .content("Great place!")
                .build());

        // when
        when(reviewRepository.findAllByPlace_KakaoPlaceId(kakaoPlaceId)).thenReturn(mockReviewList);
        List<ReviewDetailDto> reviewDetailDtoList = reviewService.findReviewsByKakaoPlaceId(kakaoPlaceId);

        // then
        verify(reviewRepository, times(1)).findAllByPlace_KakaoPlaceId(kakaoPlaceId);
        assertEquals(mockReviewList.size(), reviewDetailDtoList.size());

        for (int i = 0; i < mockReviewList.size(); i++) {
            Review review = mockReviewList.get(i);
            ReviewDetailDto reviewDetailDto = reviewDetailDtoList.get(i);

            assertEquals(review.getId(), reviewDetailDto.getId());
            assertEquals(review.getPlace().getKakaoPlaceId(), reviewDetailDto.getKakaoPlaceId());
            assertEquals(review.getUser().getId(), reviewDetailDto.getUser().getId());
        }
    }

    @Test
    @DisplayName("음식점별 리뷰 조회 (실패 - 존재하지 않는 음식점 ID)")
    void getReviewsByKakaoPlace_failure() {
        // given
        Long kakaoPlaceId = 1L;

        // when
        when(reviewRepository.findAllByPlace_KakaoPlaceId(kakaoPlaceId)).thenReturn(Collections.emptyList());
        List<ReviewDetailDto> reviewDetailDtoList = reviewService.findReviewsByKakaoPlaceId(kakaoPlaceId);

        // then
        verify(reviewRepository, times(1)).findAllByPlace_KakaoPlaceId(kakaoPlaceId);
        assertTrue(reviewDetailDtoList.isEmpty());
    }

}