package com.daedongmap.daedongmap.follow.controller;

import com.daedongmap.daedongmap.follow.dto.FollowerDto;
import com.daedongmap.daedongmap.follow.dto.FollowingDto;
import com.daedongmap.daedongmap.follow.service.FollowService;
import com.daedongmap.daedongmap.user.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/api/follows")
    @Operation(summary = "팔로우하기", description = "상대방을 팔로우 합니다.")
    public ResponseEntity<?> doFollow(@AuthenticationPrincipal CustomUserDetails tokenUser,
                                      @RequestParam Long followingId) {
        Long followerId = tokenUser.getUser().getId();
        log.info("팔로우하기 api 호출 - 팔로잉 상대 : " + followingId);

        followService.doFollow(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/follows")
    @Operation(summary = "팔로우 취소하기", description = "상대방을 팔로우 취소합니다.")
    public ResponseEntity<?> unFollow(@AuthenticationPrincipal CustomUserDetails tokenUser,
                                      @RequestParam Long followingId) {
        Long followerId = tokenUser.getUser().getId();
        log.info("팔로우 취소 api 호출 - 팔로잉 취소 상대 : " + followingId);

        followService.unFollow(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/follows/following")
    @Operation(summary = "팔로잉 리스트 조회", description = "사용자가 팔로잉한 유저 리스트를 조회합니다.")
    public ResponseEntity<List<FollowingDto>> getFollowingList(@AuthenticationPrincipal CustomUserDetails tokenUser) {
        Long userId = tokenUser.getUser().getId();
        log.info("내가 팔로잉한 리스트 api 호출 - 나 : " + userId);

        List<FollowingDto> followingDtoList = followService.getFollowingList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(followingDtoList);
    }

    @GetMapping("/api/follows/follower")
    @Operation(summary = "팔로워 리스트 조회", description = "사용자의 팔로워 리스트를 조회합니다.")
    public ResponseEntity<List<FollowerDto>> getFollowerList(@AuthenticationPrincipal CustomUserDetails tokenUser) {
        Long userId = tokenUser.getUser().getId();
        log.info("나의 팔로워 리스트 api 호출 - 나 : " + userId);

        List<FollowerDto> followerDtoList = followService.getFollowerList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(followerDtoList);
    }

}
