package com.daedongmap.daedongmap.place.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PlaceBasicInfoDto {
    private Long Id;
    private String name;
    private String category;
    private Double latitude; // 위도
    private Double longitude; // 경도
}
