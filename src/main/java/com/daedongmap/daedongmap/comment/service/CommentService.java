package com.daedongmap.daedongmap.comment.service;

import com.daedongmap.daedongmap.comment.domain.Comment;
import com.daedongmap.daedongmap.comment.dto.CommentDto;
import com.daedongmap.daedongmap.comment.dto.CommentCreateDto;
import com.daedongmap.daedongmap.comment.dto.CommentUpdateDto;
import com.daedongmap.daedongmap.comment.dto.CommentWithRepliesDto;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public CommentDto createComment(CommentCreateDto commentCreateDto) {
        Users user = userRepository.findById(commentCreateDto.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Review review = reviewRepository.findById(commentCreateDto.getReviewId()).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        Comment comment = Comment.builder()
                .user(user)
                .review(review)
                .content(commentCreateDto.getContent())
                .parentId(commentCreateDto.getParentId())
                .build();

        Comment createdComment = commentRepository.save(comment);
        return toCommentBasicInfoDto(createdComment);
    }

    @Transactional(readOnly = true)
    public List<CommentWithRepliesDto> findCommentsByReview(Long reviewId) {
        List<Comment> comments = commentRepository.findAllByReviewId(reviewId);
        List<CommentDto> commentDtos = comments.stream()
                .map(this::toCommentBasicInfoDto)
                .collect(Collectors.toList());

        List<CommentWithRepliesDto> commentWithRepliesDtoList = new ArrayList<>();
        for (CommentDto commentDto: commentDtos) {
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
    public CommentDto updateComment(Long commentId, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        comment.updateComment(commentUpdateDto);
        return toCommentBasicInfoDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    private CommentDto toCommentBasicInfoDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .user(UserBasicInfoDto.builder()
                        .id(comment.getUser().getId())
                        .nickName(comment.getUser().getNickName())
                        .email(comment.getUser().getEmail())
                        .build())
                .reviewId(comment.getReview().getId())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .build();
    }

}
