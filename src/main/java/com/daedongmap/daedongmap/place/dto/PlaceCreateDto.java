package com.daedongmap.daedongmap.place.dto;

import lombok.Getter;

@Getter
public class PlaceCreateDto {
    private Long userId;
    private String name;
    private String category;

    private String addrSigungu; //시군구
    private String addrJibun; //지번 주소
    private String addrRoad; //도로명 주소
    private String addrDetail; //상세 주소

    private Double latitude; // 위도
    private Double longitude; // 경도
}
