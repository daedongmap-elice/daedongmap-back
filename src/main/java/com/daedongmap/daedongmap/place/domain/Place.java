package com.daedongmap.daedongmap.place.domain;

import com.daedongmap.daedongmap.common.entity.BaseTimeEntity;
import com.daedongmap.daedongmap.place.dto.PlaceUpdateDto;
import com.daedongmap.daedongmap.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class Place extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private Long kakaoPlaceId;
    private String placeName;
    private String placeUrl;
    private String categoryName;
    private String addressName;
    private String roadAddressName;
    private String phone;

    private Double x;
    private Double y;

    private float averageRating;

    @Builder
    public Place(Long id, Long kakaoPlaceId, String placeName, String placeUrl, String categoryName, String addressName, String roadAddressName, String phone, Double x, Double y, float averageRating) {
        this.id = id;
        this.kakaoPlaceId = kakaoPlaceId;
        this.placeName = placeName;
        this.placeUrl = placeUrl;
        this.categoryName = categoryName;
        this.addressName = addressName;
        this.roadAddressName = roadAddressName;
        this.phone = phone;
        this.x = x;
        this.y = y;
        this.averageRating = averageRating;
    }

    public void updatePlace(PlaceUpdateDto placeUpdateDto) {
        this.kakaoPlaceId = placeUpdateDto.getKakaoPlaceId();
        this.placeName = placeUpdateDto.getPlaceName();
        this.placeUrl = placeUpdateDto.getPlaceUrl();
        this.categoryName = placeUpdateDto.getCategoryName();
        this.addressName = placeUpdateDto.getAddressName();
        this.roadAddressName = placeUpdateDto.getRoadAddressName();
        this.phone = placeUpdateDto.getPhone();
        this.x = placeUpdateDto.getX();
        this.y = placeUpdateDto.getY();
        this.averageRating = placeUpdateDto.getAverageRating();
    }

}
