package com.daedongmap.daedongmap.comment.controller;

import com.daedongmap.daedongmap.comment.dto.CommentBasicInfoDto;
import com.daedongmap.daedongmap.comment.dto.CommentCreateDto;
import com.daedongmap.daedongmap.comment.dto.CommentUpdateDto;
import com.daedongmap.daedongmap.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/api/comments")
    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    public ResponseEntity<CommentBasicInfoDto> createComment(@RequestBody CommentCreateDto commentCreateDto) {
        CommentBasicInfoDto commentBasicInfoDto = commentService.createComment(commentCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentBasicInfoDto);
    }

    @GetMapping("/api/comments/reviews/{reviewId}")
    @Operation(summary = "리뷰별 댓글 조회", description = "리뷰에 대한 댓글을 조회합니다.")
    public ResponseEntity<List<CommentBasicInfoDto>> findCommentByReview(@PathVariable Long reviewId) {
        List<CommentBasicInfoDto> findCommentDtos = commentService.findCommentsByReview(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(findCommentDtos);
    }

    @PutMapping("/api/comments/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    public ResponseEntity<CommentBasicInfoDto> modifyComment(@PathVariable Long commentId, @RequestBody CommentUpdateDto commentUpdateDto) {
        CommentBasicInfoDto commentBasicInfoDto = commentService.updateComment(commentId, commentUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(commentBasicInfoDto);
    }

    @DeleteMapping("/api/comments/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

}
