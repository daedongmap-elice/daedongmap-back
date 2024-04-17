package com.daedongmap.daedongmap.mediafile;

import com.daedongmap.daedongmap.review.Review;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Mediafile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="user_id")
    @ManyToOne
    private User user;

//    미디어 파일은 리뷰에 등록하는 거니까 장소 정보가 필요없을 수 있음
//    private Place place;

    @JoinColumn(name="review_id")
    @ManyToOne
    private Review review;

    private String filePath;

    @Column(updatable = false)
    private LocalDateTime uploadedAt;

}
