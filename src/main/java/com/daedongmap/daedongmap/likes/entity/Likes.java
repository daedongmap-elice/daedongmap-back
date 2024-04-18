package com.daedongmap.daedongmap.likes.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="user_id")
    @ManyToOne
    private User user;

    @JoinColumn(name="review_id")
    @ManyToOne
    private Review review;

}
