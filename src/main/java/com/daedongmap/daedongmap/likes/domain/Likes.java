package com.daedongmap.daedongmap.likes.domain;

import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.user.domain.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
public class Likes {

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

    public Likes(Users user, Review review) {
        this.user = user;
        this.review = review;
    }

}
