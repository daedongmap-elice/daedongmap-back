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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        return new CommentDto(createdComment);
    }

    @Transactional(readOnly = true)
    public List<CommentWithRepliesDto> findCommentsByReview(Long reviewId) {
        List<Comment> comments = commentRepository.findAllByReviewId(reviewId);
        List<CommentDto> commentDtos = comments.stream()
                .map(CommentDto::new)
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
        return new CommentDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        List<Comment> replies = commentRepository.findAllByParentId(commentId);
        for (Comment reply : replies) {
            commentRepository.deleteById(reply.getId());
        }
        commentRepository.deleteById(commentId);
    }

}
