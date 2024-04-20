package com.daedongmap.daedongmap.place.domain;

import com.daedongmap.daedongmap.user.domain.Users;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name="user_id")
    @ManyToOne
    private Users user;
    private String name;
    private String category;

    private String addrSigungu; //시군구
    private String addrJibun; //지번 주소
    private String addrRoad; //도로명 주소
    private String addrDetail; //상세 주소

    private Double latitude; // 위도
    private Double longitude; // 경도

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
