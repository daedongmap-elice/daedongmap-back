package com.daedongmap.daedongmap.place.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceCreateDto {
    private Long userId;
    private Long kakaoPlaceId;
    private String placeName;
    private String placeUrl;
    private String categoryName;
    private String addressName;
    private String roadAddressName;
    private String phone;

    private Double x;
    private Double y;
}
