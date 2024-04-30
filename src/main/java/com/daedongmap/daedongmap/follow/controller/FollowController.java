package com.daedongmap.daedongmap.follow.controller;

import com.daedongmap.daedongmap.follow.dto.FollowerDto;
import com.daedongmap.daedongmap.follow.dto.FollowingDto;
import com.daedongmap.daedongmap.follow.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/api/follows")
    @Operation(summary = "팔로우하기", description = "상대방을 팔로우 합니다.")
    public ResponseEntity<?> doFollow(@RequestParam Long followerId, @RequestParam Long followingId) {
        followService.doFollow(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/follows")
    @Operation(summary = "팔로우 취소하기", description = "상대방을 팔로우 취소합니다.")
    public ResponseEntity<?> unFollow(@RequestParam Long followerId, @RequestParam Long followingId) {
        followService.unFollow(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/follows/users/{userId}/following")
    @Operation(summary = "팔로잉 리스트 조회", description = "사용자가 팔로잉한 유저 리스트를 조회합니다.")
    public ResponseEntity<List<FollowingDto>> getFollowingList(@PathVariable Long userId) {
        List<FollowingDto> followingDtoList = followService.getFollowingList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(followingDtoList);
    }

    @GetMapping("/api/follows/users/{userId}/follower")
    @Operation(summary = "팔로워 리스트 조회", description = "사용자의 팔로워 리스트를 조회합니다.")
    public ResponseEntity<List<FollowerDto>> getFollowerList(@PathVariable Long userId) {
        List<FollowerDto> followerDtoList = followService.getFollowerList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(followerDtoList);
    }

}
