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
    @ManyToOne
    private Users user;

    @JoinColumn(name="review_id")
    @ManyToOne
    private Review review;

    private String content;

    @Builder
    public Comment(Users user, Review review, String content) {
        this.user = user;
        this.review = review;
        this.content = content;
    }

    public void updateComment(CommentUpdateDto commentUpdateDto) {
        this.content = commentUpdateDto.getContent();
    }

}
