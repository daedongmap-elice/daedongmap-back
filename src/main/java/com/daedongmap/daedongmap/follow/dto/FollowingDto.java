package com.daedongmap.daedongmap.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FollowingDto {

    private Long followingId; // 팔로잉한 유저 아이디
    private boolean isFollower; // 팔로잉한 유저가 역으로 팔로우를 해주는지 (맞팔인지)

}
