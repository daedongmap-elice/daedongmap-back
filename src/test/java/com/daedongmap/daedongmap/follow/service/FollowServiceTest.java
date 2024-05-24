package com.daedongmap.daedongmap.follow.service;

import com.daedongmap.daedongmap.alarm.service.AlarmService;
import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.follow.dto.FollowerDto;
import com.daedongmap.daedongmap.follow.dto.FollowingDto;
import com.daedongmap.daedongmap.follow.model.Follow;
import com.daedongmap.daedongmap.follow.repository.FollowRepository;
import com.daedongmap.daedongmap.likes.repository.LikeRepository;
import com.daedongmap.daedongmap.likes.service.LikeService;
import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private FollowRepository followRepository;
    @Mock
    private AlarmService alarmService;
    @InjectMocks
    private FollowService followService;


    @Test
    @DisplayName("팔로우 하기 (성공)")
    void doFollow_success() {
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

        Follow mockFollow = new Follow(mockUser1, mockUser2);

        lenient().when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));
        lenient().when(userRepository.findById(mockUser2.getId())).thenReturn(Optional.of(mockUser2));
        lenient().when(followRepository.findByFollowerAndFollowing(mockUser1, mockUser2)).thenReturn(null);

        // when
        followService.doFollow(mockUser1.getId(), mockUser2.getId());

        // then
        verify(followRepository, times(1)).save(any(Follow.class));
        verify(alarmService, times(1)).sendToClient(mockFollow.getFollowing().getId(), "You have a new follower - " + mockFollow.getFollower().getId());
    }

    @Test
    @DisplayName("팔로우 하기 (실패 - 이미 팔로우를 한 경우)")
    void doFollow_alreadyFollowed_failure() {
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

        Follow mockFollow = new Follow(mockUser1, mockUser2);

        lenient().when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));
        lenient().when(userRepository.findById(mockUser2.getId())).thenReturn(Optional.of(mockUser2));
        lenient().when(followRepository.findByFollowerAndFollowing(mockUser1, mockUser2)).thenReturn(mockFollow);

        // when
        CustomException exception = assertThrows(
                CustomException.class, () -> followService.doFollow(mockUser1.getId(), mockUser2.getId())
        );

        // then
        assertEquals(ErrorCode.FOLLOW_DUPLICATED, exception.getErrorCode());
        verify(followRepository, never()).save(any(Follow.class));
        verify(alarmService, never()).sendToClient(anyLong(), anyString());
    }

    @Test
    @DisplayName("팔로우 하기 (실패 - 본인을 팔로우한 경우)")
    void doFollow_myself_failure() {
        // given
        Users mockUser = Users.builder()
                .id(1L)
                .nickName("mock-user")
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> followService.doFollow(mockUser.getId(), mockUser.getId()));

        // then
        verify(followRepository, never()).save(any(Follow.class));
        verify(alarmService, never()).sendToClient(anyLong(), anyString());
        assertEquals(ErrorCode.FOLLOW_MYSELF_NOW_ALLOWED, exception.getErrorCode());
    }

    @Test
    @DisplayName("팔로우 취소 (성공)")
    void unFollow_success() {
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

        Follow mockFollow = new Follow(mockUser1, mockUser2);

        when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(mockUser2.getId())).thenReturn(Optional.of(mockUser2));
        when(followRepository.findByFollowerAndFollowing(mockUser1, mockUser2)).thenReturn(mockFollow);

        // when
        followService.unFollow(mockUser1.getId(), mockUser2.getId());

        // then
        verify(followRepository, times(1)).delete(mockFollow);
        assertFalse(followRepository.existsByFollowerAndFollowing(mockUser1, mockUser2));
    }

    @Test
    @DisplayName("팔로우 취소 (실패 - 팔로우한 적이 없는 경우)")
    void unFollow_followNotFound_failure() {
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

        when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(mockUser2.getId())).thenReturn(Optional.of(mockUser2));
        when(followRepository.findByFollowerAndFollowing(mockUser1, mockUser2)).thenReturn(null);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> followService.unFollow(mockUser1.getId(), mockUser2.getId()));

        // then
        verify(followRepository, never()).delete(any(Follow.class));
        assertEquals(ErrorCode.FOLLOW_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팔로우 취소 (실패 - 본인을 팔로우 취소하려는 경우)")
    void unFollow_myself_failure() {
        // given
        Users mockUser1 = Users.builder()
                .id(1L)
                .nickName("mock-user-1")
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> followService.unFollow(mockUser1.getId(), mockUser1.getId()));

        // then
        verify(followRepository, never()).delete(any(Follow.class));
        assertEquals(ErrorCode.UNFOLLOW_MYSELF_NOW_ALLOWED, exception.getErrorCode());
    }

    @Test
    @DisplayName("팔로잉 리스트 조회 (성공)")
    void getFollowingList_success() {
        // given
        // mockFollower가 mockUser1과 mockUser2를 팔로잉함
        Users mockUser1 = Users.builder()
                .id(1L)
                .nickName("mockUser1")
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        Users mockUser2 = Users.builder()
                .id(2L)
                .nickName("mockUser2")
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        Users mockFollower = Users.builder()
                .id(3L)
                .nickName("mockFollower")
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        Follow mockFollow1 = new Follow(mockFollower, mockUser1);
        Follow mockFollow2 = new Follow(mockFollower, mockUser2);

        List<Follow> mockFollowingList = new ArrayList<>();
        mockFollowingList.add(mockFollow1);
        mockFollowingList.add(mockFollow2);

        lenient().when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));
        lenient().when(userRepository.findById(mockUser2.getId())).thenReturn(Optional.of(mockUser1));
        lenient().when(userRepository.findById(mockFollower.getId())).thenReturn(Optional.of(mockFollower));

        lenient().when(followRepository.findByFollowerAndFollowing(mockFollower, mockUser1)).thenReturn(mockFollow1);
        lenient().when(followRepository.findByFollowerAndFollowing(mockFollower, mockUser2)).thenReturn(mockFollow2);
        lenient().when(followRepository.findAllByFollower(mockFollower)).thenReturn(mockFollowingList);

        // when
        List<FollowingDto> followingDtoList = followService.getFollowingList(mockFollower.getId());

        // then
        assertNotNull(followingDtoList);
        assertEquals(mockFollowingList.size(), followingDtoList.size());
        for (int i = 0; i < mockFollowingList.size(); i++) {
            assertEquals(mockFollowingList.get(i).getFollowing().getNickName(), followingDtoList.get(i).getFollowing().getNickName());
        }
    }

    @Test
    @DisplayName("팔로워 리스트 조회 (성공)")
    void getFollowerList_success() {
        // given
        // mockFollower1과 mockFollower2가 mockUser의 팔로워임
        Users mockFollower1 = Users.builder()
                .id(1L)
                .nickName("mockFollower1")
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        Users mockFollower2 = Users.builder()
                .id(2L)
                .nickName("mockFollower2")
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        Users mockUser = Users.builder()
                .id(3L)
                .nickName("mockUser")
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        Follow mockFollow1 = new Follow(mockFollower1, mockUser);
        Follow mockFollow2 = new Follow(mockFollower2, mockUser);

        List<Follow> mockFollowingList = new ArrayList<>();
        mockFollowingList.add(mockFollow1);
        mockFollowingList.add(mockFollow2);

        lenient().when(userRepository.findById(mockFollower1.getId())).thenReturn(Optional.of(mockFollower1));
        lenient().when(userRepository.findById(mockFollower2.getId())).thenReturn(Optional.of(mockFollower2));
        lenient().when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        lenient().when(followRepository.findByFollowerAndFollowing(mockFollower1, mockUser)).thenReturn(mockFollow1);
        lenient().when(followRepository.findByFollowerAndFollowing(mockFollower2, mockUser)).thenReturn(mockFollow2);
        lenient().when(followRepository.findAllByFollowing(mockUser)).thenReturn(mockFollowingList);

        // when
        List<FollowerDto> followerDtoList = followService.getFollowerList(mockUser.getId());

        // then
        assertNotNull(followerDtoList);
        assertEquals(mockFollowingList.size(), followerDtoList.size());
        for (int i = 0; i < mockFollowingList.size(); i++) {
            assertEquals(mockFollowingList.get(i).getFollower().getNickName(), followerDtoList.get(i).getFollower().getNickName());
        }
    }

}