package com.daedongmap.daedongmap.comment.service;

import com.daedongmap.daedongmap.comment.domain.Comment;
import com.daedongmap.daedongmap.comment.dto.CommentDto;
import com.daedongmap.daedongmap.comment.repository.CommentRepository;
import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import com.daedongmap.daedongmap.user.domain.Authority;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;


    private Users createMockUser(Long userId, String nickName) {
        return Users.builder()
                .id(userId)
                .nickName(nickName)
                .isMember(true)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();
    }

    private Place createMockPlace(Long kakaoPlaceId, String placeName, String categoryName) {
        return Place.builder()
                .kakaoPlaceId(kakaoPlaceId)
                .placeName(placeName)
                .categoryName(categoryName)
                .build();
    }

    private Review createMockReview(Long reviewId, Place place, Users user, String content) {
        return Review.builder()
                .id(reviewId)
                .place(place)
                .user(user)
                .content(content)
                .hygieneRating(4.0f)
                .tasteRating(4.5f)
                .kindnessRating(5.0f)
                .averageRating(4.5f)
                .build();
    }

    private Comment createMockComment(Long commentId, Users user, Review review, String content, Long parentId) {
        return Comment.builder()
                .id(commentId)
                .user(user)
                .review(review)
                .content(content)
                .parentId(parentId)
                .build();
    }

    @Test
    @DisplayName("내가 작성한 댓글 조회 (성공 - 댓글이 존재하는 경우)")
    void getCommentsByMe_success() {
        // given
        Users mockUser = createMockUser(1L, "mock User");
        Place mockPlace = createMockPlace(1000L, "mockPlace", "한식");
        Review mockReview = createMockReview(1L, mockPlace, mockUser, "Mock review content");

        List<Comment> mockCommentList = new ArrayList<>();
        mockCommentList.add(createMockComment(1L, mockUser, mockReview, "Mock comment content", null));
        mockCommentList.add(createMockComment(2L, mockUser, mockReview, "Mock comment content", null));

        lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        lenient().when(commentRepository.findAllByUserId(mockUser.getId())).thenReturn(mockCommentList);

        // when
        List<CommentDto> commentDtoList = commentService.findCommentsByMe(mockUser.getId());

        // then
        verify(commentRepository, times(1)).findAllByUserId(mockUser.getId());

        assertNotNull(commentDtoList);
        assertEquals(mockCommentList.size(), commentDtoList.size());
    }

    @Test
    @DisplayName("내가 작성한 댓글 조회 (성공 - 댓글이 존재하지 않 경우)")
    void getCommentsByMe_empty_success() {
        // given
        Users mockUser = createMockUser(1L, "mock User");

        List<Comment> mockCommentList = new ArrayList<>();

        lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        lenient().when(commentRepository.findAllByUserId(mockUser.getId())).thenReturn(mockCommentList);

        // when
        List<CommentDto> commentDtoList = commentService.findCommentsByMe(mockUser.getId());

        // then
        verify(commentRepository, times(1)).findAllByUserId(mockUser.getId());

        assertNotNull(commentDtoList);
        assertEquals(0, commentDtoList.size());
    }

}
