package com.daedongmap.daedongmap.review.domain;

import com.daedongmap.daedongmap.common.entity.BaseTimeEntity;
import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.review.dto.ReviewUpdateDto;
import com.daedongmap.daedongmap.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.URL;

@Entity
@Getter
@RequiredArgsConstructor
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="user_id")
    @ManyToOne
    private Users user;

    @JoinColumn(name="place_id")
    @ManyToOne
    private Place place;

    private String title;

    private String content;

    private float tasteRating;

    private float hygieneRating;

    private float kindnessRating;

    private float averageRating;

    @Builder
    public Review(Users user, Place place, String title, String content, float hygieneRating, float tasteRating, float kindnessRating, float averageRating) {
        this.user = user;
        this.place = place;
        this.title = title;
        this.content = content;
        this.hygieneRating = hygieneRating;
        this.tasteRating = tasteRating;
        this.kindnessRating = kindnessRating;
        this.averageRating = averageRating;
    }

    public void updateReview(ReviewUpdateDto reviewUpdateDto) {
        this.title = reviewUpdateDto.getTitle();
        this.content = reviewUpdateDto.getContent();
        this.hygieneRating = reviewUpdateDto.getHygieneRating();
        this.tasteRating = reviewUpdateDto.getTasteRating();
        this.kindnessRating = reviewUpdateDto.getKindnessRating();
        this.averageRating = reviewUpdateDto.getAverageRating();
    }

}
