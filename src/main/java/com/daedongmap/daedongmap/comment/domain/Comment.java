package com.daedongmap.daedongmap.comment.domain;

import com.daedongmap.daedongmap.comment.dto.CommentUpdateDto;
import com.daedongmap.daedongmap.common.entity.BaseTimeEntity;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="user_id")
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Users user;

    @JoinColumn(name="review_id")
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Review review;

    private String content;

    private Long parentId = null;

    @Builder
    public Comment(Users user, Review review, String content, Long parentId) {
        this.user = user;
        this.review = review;
        this.content = content;
        this.parentId = parentId;
    }

    public void updateComment(CommentUpdateDto commentUpdateDto) {
        this.content = commentUpdateDto.getContent();
    }

}
