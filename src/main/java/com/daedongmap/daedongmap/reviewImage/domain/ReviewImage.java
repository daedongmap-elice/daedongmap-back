package com.daedongmap.daedongmap.reviewImage.domain;

import com.daedongmap.daedongmap.common.entity.BaseTimeEntity;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
public class ReviewImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="user_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    @JoinColumn(name="review_id")
    @ManyToOne
    private Review review;

    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Builder
    public ReviewImage(Users user, Review review, String filePath, String fileName) {
        this.user = user;
        this.review = review;
        this.fileName = fileName;
        this.filePath = filePath;
    }

}
