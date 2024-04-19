package com.daedongmap.daedongmap.review.domain;

import com.daedongmap.daedongmap.review.dto.ReviewUpdateDto;
import com.daedongmap.daedongmap.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="user_id")
    @ManyToOne
    private Users user;

//    @JoinColumn(name="place_id")
//    @ManyToOne
//    private Place place;

    private String title;

    private String content;

    private float rating;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public Review(Users user, String title, String content, float rating) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.rating = rating;
    }

    public void updateReview(ReviewUpdateDto reviewUpdateDto) {
        this.title = reviewUpdateDto.getTitle();
        this.content = reviewUpdateDto.getContent();
        this.rating = reviewUpdateDto.getRating();
    }

}
