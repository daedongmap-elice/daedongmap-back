package com.daedongmap.daedongmap.review.domain;

import com.daedongmap.daedongmap.common.entity.BaseTimeEntity;
import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.review.dto.ReviewUpdateDto;
import com.daedongmap.daedongmap.reviewImage.domain.ReviewImage;
import com.daedongmap.daedongmap.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    @JoinColumn(name="kakao_place_id", referencedColumnName="kakaoPlaceId")
    @ManyToOne
    private Place place;

    private String content;

    private float tasteRating;

    private float hygieneRating;

    private float kindnessRating;

    private float averageRating;

    @OneToMany(mappedBy = "review")
    private List<ReviewImage> reviewImageList = new ArrayList<>();

    @Builder
    public Review(Users user, Place place, String content, float hygieneRating, float tasteRating, float kindnessRating, float averageRating) {
        this.user = user;
        this.place = place;
        this.content = content;
        this.hygieneRating = hygieneRating;
        this.tasteRating = tasteRating;
        this.kindnessRating = kindnessRating;
        this.averageRating = averageRating;
    }

    public void updateReview(ReviewUpdateDto reviewUpdateDto) {
        this.content = reviewUpdateDto.getContent();
        this.hygieneRating = reviewUpdateDto.getHygieneRating();
        this.tasteRating = reviewUpdateDto.getTasteRating();
        this.kindnessRating = reviewUpdateDto.getKindnessRating();
        this.averageRating = reviewUpdateDto.getAverageRating();
    }

    public void addReviewImage(ReviewImage reviewImage) {
        if (reviewImage != null) {
            reviewImageList.add(reviewImage);
        }
    }

}
