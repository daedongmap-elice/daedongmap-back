package com.daedongmap.daedongmap.likes.controller;

import com.daedongmap.daedongmap.likes.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/api/likes")
    @Operation(summary = "좋아요 생성", description = "좋아요를 생성합니다.")
    public void likeReview(@RequestParam Long userId, @RequestParam Long reviewId) {
        likeService.likeReview(userId, reviewId);
    }

    @DeleteMapping("/api/likes")
    @Operation(summary = "좋아요 취소", description = "좋아요를 취소합니다.")
    public void unlikeReview(@RequestParam Long reviewId, @RequestParam Long userId) {
        likeService.unlikeReview(reviewId, userId);
    }

}
