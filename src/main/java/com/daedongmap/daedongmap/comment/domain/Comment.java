package com.daedongmap.daedongmap.comment.domain;

import com.daedongmap.daedongmap.common.entity.BaseTimeEntity;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@RequiredArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="user_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    @JoinColumn(name="review_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
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

}
