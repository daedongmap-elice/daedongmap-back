package com.daedongmap.daedongmap.follow.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.follow.model.Follow;
import com.daedongmap.daedongmap.follow.repository.FollowRepository;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public void doFollow(Long followerId, Long followingId) {
        Users follower = userRepository.findById(followerId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Users following = userRepository.findById(followingId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 자기 자신은 팔로우 할 수 없음
        if (followerId == followingId) {
            throw new CustomException(ErrorCode.FOLLOW_MYSELF_NOW_ALLOWED);
        }

        // 이미 팔로우 중인지 확인
        if (!isFollowing(follower, following)) {
            throw new CustomException(ErrorCode.FOLLOW_DUPLICATED);
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);
    }

    public boolean isFollowing(Users follower, Users following) {
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    @Transactional
    public void unFollow(Long followerId, Long followingId) {
        Users follower = userRepository.findById(followerId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Users following = userRepository.findById(followingId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following);
        if (follow != null) {
            followRepository.delete(follow);
        }
    }

}
