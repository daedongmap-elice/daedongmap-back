package com.daedongmap.daedongmap.follow.dto;

import com.daedongmap.daedongmap.user.dto.UserBasicInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FollowerDto {

    private UserBasicInfoDto follower; // 유저를 팔로잉한 팔로워
    private boolean isFollowing; // 유저의 팔로워를 유저도 팔로잉하는지

}
