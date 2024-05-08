package com.daedongmap.daedongmap.comment.service;

import com.daedongmap.daedongmap.comment.domain.Comment;
import com.daedongmap.daedongmap.comment.dto.CommentDto;
import com.daedongmap.daedongmap.comment.dto.CommentCreateDto;
import com.daedongmap.daedongmap.comment.dto.CommentWithRepliesDto;
import com.daedongmap.daedongmap.comment.repository.CommentRepository;
import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public CommentDto createComment(Long userId, CommentCreateDto commentCreateDto) {
        Users user = getUserById(userId);
        Review review = getReviewById(commentCreateDto.getReviewId());

        Comment comment = Comment.builder()
                .user(user)
                .review(review)
                .content(commentCreateDto.getContent())
                .parentId(commentCreateDto.getParentId())
                .build();

        Comment createdComment = commentRepository.save(comment);
        return new CommentDto(createdComment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findCommentsByMe(Long userId) {
        List<Comment> comments = commentRepository.findAllByUserId(userId);
        return comments.stream()
                .map(CommentDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CommentWithRepliesDto> findCommentsByReview(Long reviewId) {
        List<Comment> comments = commentRepository.findAllByReviewId(reviewId);
        List<CommentDto> commentDtoList = comments.stream()
                .map(CommentDto::new)
                .toList();

        List<CommentWithRepliesDto> commentWithRepliesDtoList = new ArrayList<>();
        for (CommentDto commentDto: commentDtoList) {
            if (commentDto.getParentId() == null) {
                commentWithRepliesDtoList.add(new CommentWithRepliesDto(commentDto));
            }
            else {
                for (CommentWithRepliesDto existingComment : commentWithRepliesDtoList) {
                    if (existingComment.getId().equals(commentDto.getParentId())) {
                        existingComment.addReply(commentDto);
                        break;
                    }
                }
            }
        }
        return commentWithRepliesDtoList;
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!isCommentOwner(userId, comment)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_MINE);
        }

        List<Comment> replies = commentRepository.findAllByParentId(commentId);
        commentRepository.deleteAll(replies);
        commentRepository.deleteById(commentId);
    }

    private Users getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }

    private boolean isCommentOwner(Long userId, Comment comment) {
        return comment.getUser().getId().equals(userId);
    }

}
