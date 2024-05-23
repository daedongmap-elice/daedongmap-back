package com.daedongmap.daedongmap.likes.service;

import com.daedongmap.daedongmap.alarm.service.AlarmService;
import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.likes.domain.Likes;
import com.daedongmap.daedongmap.likes.repository.LikeRepository;
import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import com.daedongmap.daedongmap.user.domain.Authority;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private AlarmService alarmService;
    @InjectMocks
    private LikeService likeService;


    @Test
    @DisplayName("좋아요 생성 (성공)")
    void likeReview_success() {
        // given
        Users mockUser1 = Users.builder()
                .id(1L)
                .nickName("mock-user-1")
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        Users mockUser2 = Users.builder()
                .id(2L)
                .nickName("mock-user-2")
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        Place mockPlace = Place.builder()
                .kakaoPlaceId(1000L)
                .placeName("mock-place")
                .categoryName("일식")
                .build();

        Review mockReview = Review.builder()
                .id(1L)
                .place(mockPlace)
                .user(mockUser1)
                .content("content")
                .hygieneRating(4.0f)
                .tasteRating(4.0f)
                .kindnessRating(5.0f)
                .averageRating(4.2f)
                .build();

        lenient().when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));
        lenient().when(userRepository.findById(mockUser2.getId())).thenReturn(Optional.of(mockUser2));
        lenient().when(reviewRepository.findById(mockReview.getId())).thenReturn(Optional.of(mockReview));
        lenient().when(likeRepository.findByUserAndReview(mockUser2, mockReview)).thenReturn(null);

        // when
        likeService.likeReview(mockUser2.getId(), mockReview.getId());

        // then
        verify(likeRepository, times(1)).findByUserAndReview(mockUser2, mockReview);
        verify(alarmService).sendToClient(mockReview.getUser().getId(), "Your review got a Like by user - " + mockUser2.getId());
    }

    @Test
    @DisplayName("좋아요 생성 (실패 - 이미 좋아요를 누른 경우)")
    void likeReview_alreadyLiked_failure() {
        // given
        Users mockUser1 = Users.builder()
                .id(1L)
                .nickName("mock-user-1")
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        Users mockUser2 = Users.builder()
                .id(2L)
                .nickName("mock-user-2")
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        Place mockPlace = Place.builder()
                .kakaoPlaceId(1000L)
                .placeName("mock-place")
                .categoryName("일식")
                .build();

        Review mockReview = Review.builder()
                .id(1L)
                .place(mockPlace)
                .user(mockUser1)
                .content("content")
                .hygieneRating(4.0f)
                .tasteRating(4.0f)
                .kindnessRating(5.0f)
                .averageRating(4.2f)
                .build();

        lenient().when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));
        lenient().when(userRepository.findById(mockUser2.getId())).thenReturn(Optional.of(mockUser2));
        lenient().when(reviewRepository.findById(mockReview.getId())).thenReturn(Optional.of(mockReview));
        lenient().when(likeRepository.findByUserAndReview(mockUser2, mockReview)).thenReturn(new Likes(mockUser2, mockReview));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> likeService.likeReview(mockUser2.getId(), mockReview.getId()));

        // then
        assertEquals(ErrorCode.ALREADY_LIKED, exception.getErrorCode());
        verify(likeRepository, times(1)).findByUserAndReview(mockUser2, mockReview);
        verify(alarmService, never()).sendToClient(anyLong(), anyString());
    }

}