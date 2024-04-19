package com.daedongmap.daedongmap.comment.service;

import com.daedongmap.daedongmap.comment.domain.Comment;
import com.daedongmap.daedongmap.comment.dto.CommentBasicInfoDto;
import com.daedongmap.daedongmap.comment.dto.CommentCreateDto;
import com.daedongmap.daedongmap.comment.dto.CommentUpdateDto;
import com.daedongmap.daedongmap.comment.repository.CommentRepository;
import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.UserBasicInfoDto;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public CommentBasicInfoDto createComment(CommentCreateDto commentCreateDto) {
        Users user = userRepository.findById(commentCreateDto.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Review review = reviewRepository.findById(commentCreateDto.getReviewId()).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        Comment comment = Comment.builder()
                .user(user)
                .review(review)
                .content(commentCreateDto.getContent())
                .build();

        Comment createdComment = commentRepository.save(comment);
        return toCommentBasicInfoDto(createdComment);
    }

    @Transactional(readOnly = true)
    public List<CommentBasicInfoDto> findCommentsByReview(Long reviewId) {
        List<Comment> comments = commentRepository.findAllByReviewId(reviewId);
        return comments.stream()
                .map(this::toCommentBasicInfoDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentBasicInfoDto updateComment(Long commentId, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        comment.updateComment(commentUpdateDto);
        return toCommentBasicInfoDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    private CommentBasicInfoDto toCommentBasicInfoDto(Comment comment) {
        return CommentBasicInfoDto.builder()
                .user(UserBasicInfoDto.builder()
                        .id(comment.getUser().getId())
                        .nickName(comment.getUser().getNickName())
                        .email(comment.getUser().getEmail())
                        .build())
                .reviewId(comment.getReview().getId())
                .content(comment.getContent())
                .build();
    }

}
