package com.daedongmap.daedongmap.comment.controller;

import com.daedongmap.daedongmap.comment.dto.CommentDto;
import com.daedongmap.daedongmap.comment.dto.CommentCreateDto;
import com.daedongmap.daedongmap.comment.dto.CommentWithRepliesDto;
import com.daedongmap.daedongmap.comment.service.CommentService;
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
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/comments")
    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentCreateDto commentCreateDto,
                                                    @AuthenticationPrincipal CustomUserDetails tokenUser) {
        Long userId = tokenUser.getUser().getId();
        log.info("댓글 작성 api - userId : " + userId);

        CommentDto commentBasicInfoDto = commentService.createComment(userId, commentCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentBasicInfoDto);
    }

    @GetMapping("/api/comments/reviews/{reviewId}")
    @Operation(summary = "리뷰별 댓글 조회", description = "리뷰에 대한 댓글을 조회합니다.")
    public ResponseEntity<List<CommentWithRepliesDto>> findCommentByReview(@PathVariable Long reviewId) {
        log.info("리뷰별 댓글 조회 api - reviewId : " + reviewId);

        List<CommentWithRepliesDto> commentWithRepliesDtoList = commentService.findCommentsByReview(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(commentWithRepliesDtoList);
    }

    @DeleteMapping("/api/comments/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    public void deleteComment(@PathVariable Long commentId,
                              @AuthenticationPrincipal CustomUserDetails tokenUser) {
        Long userId = tokenUser.getUser().getId();
        log.info("댓글 삭제 api - userId : " + userId + ", commentId : " + commentId);

        commentService.deleteComment(commentId, userId);
    }

}
