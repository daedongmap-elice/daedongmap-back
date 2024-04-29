package com.daedongmap.daedongmap.follow.controller;

import com.daedongmap.daedongmap.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/api/follows")
    public ResponseEntity<?> doFollow(Long followerId, Long followingId) {
        followService.doFollow(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/follows")
    public ResponseEntity<?> unFollow(Long followerId, Long followingId) {
        followService.unFollow(followerId, followingId);
        return ResponseEntity.ok().build();
    }

}
