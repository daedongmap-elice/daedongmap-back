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
import org.junit.jupiter.api.BeforeEach;
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
    @InjectMocks
    private ReviewService reviewService;

    private static final Long USER_ID = 1L;
    private static final Long KAKAO_PLACE_ID = 1L;
    private static final Long REVIEW_ID = 1L;
    private static final Long REVIEW_IMAGE_ID = 1L;

    private Users mockUser;
    private Place mockPlace;
    private Review mockReview;
    private ReviewImage mockReviewImage;
    private List<Review> mockReviewList;


    @BeforeEach
    void setUp() {
        mockUser = createMockUser();
        mockPlace = createMockPlace();
        mockReview = createMockReview();
        mockReviewImage = createMockReviewImage();
        mockReviewList = createMockReviewList();
    }

    private Users createMockUser() {
        return Users.builder()
                .id(USER_ID)
                .nickName("mockUser")
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();
    }

    private Place createMockPlace() {
        return Place.builder()
                .kakaoPlaceId(KAKAO_PLACE_ID)
                .placeName("mockPlace")
                .categoryName("한식")
                .build();
    }

    private Review createMockReview() {
        return Review.builder()
                .id(REVIEW_ID)
                .place(mockPlace)
                .user(mockUser)
                .content("mockReview")
                .hygieneRating(4.0f)
                .tasteRating(4.5f)
                .kindnessRating(5.0f)
                .averageRating(4.5f)
                .build();
    }

    private ReviewImage createMockReviewImage() {
        return ReviewImage.builder()
                .id(REVIEW_IMAGE_ID)
                .user(mockUser)
                .review(mockReview)
                .fileName("mockFileName")
                .filePath("mockFilaPath")
                .build();
    }

    private List<Review> createMockReviewList() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(mockReview);
        return reviews;
    }

    // todo : 장소가 있을 때 없을 때 둘 다 테스트해야함
    @Test
    @DisplayName("리뷰 작성 (성공)")
    void createReview_success() throws IOException {
        // given
        MultipartFile multipartFile = new MockMultipartFile("file", "fileContent".getBytes());
        List<MultipartFile> multipartFileList = new ArrayList<>();
        multipartFileList.add(multipartFile);

        PlaceCreateDto placeCreateDto = PlaceCreateDto.builder()
                .kakaoPlaceId(mockPlace.getKakaoPlaceId())
                .placeName(mockPlace.getPlaceName())
                .build();

        ReviewCreateDto reviewCreateDto = ReviewCreateDto.builder()
                .content(mockReview.getContent())
                .hygieneRating(mockReview.getHygieneRating())
                .tasteRating(mockReview.getTasteRating())
                .kindnessRating(mockReview.getKindnessRating())
                .averageRating(mockReview.getAverageRating())
                .build();

        given(userRepository.findById(USER_ID)).willReturn(Optional.of(mockUser));
        given(placeRepository.findByKakaoPlaceId(KAKAO_PLACE_ID)).willReturn(Optional.of(mockPlace));
        given(s3Service.uploadImage(multipartFile, "review")).willReturn("filePath");
        given(reviewImageRepository.save(any(ReviewImage.class))).willReturn(mockReviewImage);
        given(reviewRepository.save(any())).willReturn(mockReview);

        // when
        ReviewDto reviewDto = reviewService.createReview(USER_ID, multipartFileList, reviewCreateDto, placeCreateDto);

        // then
        verify(userRepository, times(1)).findById(USER_ID);
        verify(placeRepository, times(1)).findByKakaoPlaceId(KAKAO_PLACE_ID);
        verify(s3Service, times(1)).uploadImage(multipartFile, "review");
        verify(reviewImageRepository, times(1)).save(any(ReviewImage.class));
        verify(reviewRepository, times(1)).save(any(Review.class));

        assertNotNull(reviewDto);
        assertEquals(mockReview.getContent(), reviewDto.getContent());
        assertEquals(mockReview.getAverageRating(), reviewDto.getAverageRating());
        assertEquals(mockReview.getPlace().getKakaoPlaceId(), reviewDto.getKakaoPlaceId());

        assertNotNull(reviewDto.getReviewImageDtoList());
        assertEquals(mockReviewList.size(), reviewDto.getReviewImageDtoList().size());
        assertEquals(mockReviewImage.getFilePath(), reviewDto.getReviewImageDtoList().get(0).getFilePath());
    }

    @Test
    @DisplayName("리뷰 작성 (실패 - 존재하지 않는 사용자)")
    void createReview_userNotFound_failure() {
        // given
        MultipartFile multipartFile = new MockMultipartFile("file", "fileContent".getBytes());
        List<MultipartFile> multipartFileList = new ArrayList<>();
        multipartFileList.add(multipartFile);

        PlaceCreateDto placeCreateDto = PlaceCreateDto.builder()
                .kakaoPlaceId(mockPlace.getKakaoPlaceId())
                .placeName(mockPlace.getPlaceName())
                .build();

        ReviewCreateDto reviewCreateDto = ReviewCreateDto.builder()
                .content(mockReview.getContent())
                .hygieneRating(mockReview.getHygieneRating())
                .tasteRating(mockReview.getTasteRating())
                .kindnessRating(mockReview.getKindnessRating())
                .averageRating(mockReview.getAverageRating())
                .build();

        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            reviewService.createReview(USER_ID, multipartFileList, reviewCreateDto, placeCreateDto);
        });
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 작성 (실패 - 이미지 업로드 실패)")
    void createReview_failedFileUpload_failure() {
        // given
        MultipartFile multipartFile = new MockMultipartFile("file", "fileContent".getBytes());
        List<MultipartFile> multipartFileList = new ArrayList<>();
        multipartFileList.add(multipartFile);

        PlaceCreateDto placeCreateDto = PlaceCreateDto.builder()
                .kakaoPlaceId(mockPlace.getKakaoPlaceId())
                .placeName(mockPlace.getPlaceName())
                .build();

        ReviewCreateDto reviewCreateDto = ReviewCreateDto.builder()
                .content(mockReview.getContent())
                .hygieneRating(mockReview.getHygieneRating())
                .tasteRating(mockReview.getTasteRating())
                .kindnessRating(mockReview.getKindnessRating())
                .averageRating(mockReview.getAverageRating())
                .build();

        given(userRepository.findById(USER_ID)).willReturn(Optional.of(mockUser));
        given(placeRepository.findByKakaoPlaceId(KAKAO_PLACE_ID)).willReturn(Optional.of(mockPlace));
        given(s3Service.uploadImage(multipartFile, "review")).willThrow(new CustomException(ErrorCode.FAILED_FILE_UPLOAD));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            reviewService.createReview(USER_ID, multipartFileList, reviewCreateDto, placeCreateDto);
        });
        assertEquals(ErrorCode.FAILED_FILE_UPLOAD, exception.getErrorCode());
    }

    @Test
    @DisplayName("모든 리뷰 조회 (성공)")
    void getAllReviews_success() {
        // given
        when(reviewRepository.findAll()).thenReturn(mockReviewList);

        // when
        List<ReviewDto> reviewDtoList = reviewService.findAllReviews();

        // then
        verify(reviewRepository, times(1)).findAll();
        assertEquals(mockReviewList.size(), reviewDtoList.size());
    }

    @Test
    @DisplayName("모든 리뷰 조회 (실패)")
    void getAllReviews_failure() {
        // given
        when(reviewRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<ReviewDto> reviewDtoList = reviewService.findAllReviews();

        // then
        verify(reviewRepository, times(1)).findAll();
        assertNotEquals(mockReviewList.size(), reviewDtoList.size(), "기대했던 사이즈와 다릅니다.");
    }

    @Test
    @DisplayName("사용자별 작성한 리뷰 조회 (성공)")
    void getReviewsByUser_success() {
        // given
        when(reviewRepository.findAllByUserId(USER_ID)).thenReturn(mockReviewList);

        // when
        List<ReviewDetailDto> reviewDetailDtoList = reviewService.findReviewsByUser(USER_ID);

        // then
        verify(reviewRepository, times(1)).findAllByUserId(USER_ID);
        assertEquals(mockReviewList.size(), reviewDetailDtoList.size());

        for (int i = 0; i < mockReviewList.size(); i++) {
            Review review = mockReviewList.get(i);
            ReviewDetailDto reviewDetailDto = reviewDetailDtoList.get(i);

            assertEquals(review.getId(), reviewDetailDto.getId());
            assertEquals(review.getUser().getId(), reviewDetailDto.getUser().getId());
            assertEquals(review.getUser().getNickName(), reviewDetailDto.getUser().getNickName());
        }
    }

    @Test
    @DisplayName("사용자별 작성한 리뷰 조회 (실패 - 존재하지 않는 사용자)")
    void getReviewsByUser_userNotFound_failure() {
        // given
        when(reviewRepository.findAllByUserId(USER_ID)).thenReturn(Collections.emptyList());

        // when
        List<ReviewDetailDto> reviewDetailDtoList = reviewService.findReviewsByUser(USER_ID);

        // then
        verify(reviewRepository, times(1)).findAllByUserId(USER_ID);
        assertTrue(reviewDetailDtoList.isEmpty());
    }

    @Test
    @DisplayName("음식점별 리뷰 조회 (성공)")
    void getReviewsByKakaoPlace_success() {
        // given
        when(reviewRepository.findAllByPlace_KakaoPlaceId(KAKAO_PLACE_ID)).thenReturn(mockReviewList);

        // when
        List<ReviewDetailDto> reviewDetailDtoList = reviewService.findReviewsByKakaoPlaceId(KAKAO_PLACE_ID);

        // then
        verify(reviewRepository, times(1)).findAllByPlace_KakaoPlaceId(KAKAO_PLACE_ID);
        assertEquals(mockReviewList.size(), reviewDetailDtoList.size());

        for (int i = 0; i < mockReviewList.size(); i++) {
            Review review = mockReviewList.get(i);
            ReviewDetailDto reviewDetailDto = reviewDetailDtoList.get(i);

            assertEquals(review.getId(), reviewDetailDto.getId());
            assertEquals(review.getPlace().getKakaoPlaceId(), reviewDetailDto.getKakaoPlaceId());
            assertEquals(review.getPlace().getPlaceName(), reviewDetailDto.getPlaceName());
        }
    }

    @Test
    @DisplayName("음식점별 리뷰 조회 (실패 - 존재하지 않는 음식점)")
    void getReviewsByKakaoPlace_placeNotFound_failure() {
        // given
        when(reviewRepository.findAllByPlace_KakaoPlaceId(KAKAO_PLACE_ID)).thenReturn(Collections.emptyList());

        // when
        List<ReviewDetailDto> reviewDetailDtoList = reviewService.findReviewsByKakaoPlaceId(KAKAO_PLACE_ID);

        // then
        verify(reviewRepository, times(1)).findAllByPlace_KakaoPlaceId(KAKAO_PLACE_ID);
        assertTrue(reviewDetailDtoList.isEmpty());
    }

    @Test
    @DisplayName("리뷰 삭제 (성공)")
    void deleteReview_success() {
        // given
        given(reviewRepository.findById(REVIEW_ID)).willReturn(Optional.of(mockReview));

        // when
        reviewService.deleteReview(USER_ID, REVIEW_ID);

        // then
        verify(reviewRepository, times(1)).deleteById(REVIEW_ID);
    }

    @Test
    @DisplayName("리뷰 삭제 (실패 - 리뷰 작성자가 아닌 경우)")
    void deleteReview_notReviewOwner_failure() {
        // given
        Long notOwnerUserId = 2L;
        given(reviewRepository.findById(REVIEW_ID)).willReturn(Optional.of(mockReview));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            reviewService.deleteReview(notOwnerUserId, REVIEW_ID);
        });

        // then
        verify(reviewRepository, never()).deleteById(REVIEW_ID);
        assertEquals(ErrorCode.REVIEW_NOT_MINE, exception.getErrorCode());
    }

}