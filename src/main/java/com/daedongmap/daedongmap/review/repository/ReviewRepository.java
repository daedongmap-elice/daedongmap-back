package com.daedongmap.daedongmap.review.repository;

import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUserId(Long userId);
    List<Review> findAllByPlaceId(Long placeId);

    @Query(value = "select review " +
            "from Review review " +
            "where place.kakaoPlaceId = 27531028 order by review.createdAt limit 1")
    Review findFirstByKakaoPlaceIdOrderByCreatedAtDesc(@Param("kakaoPlaceId") Long kakaoPlaceId);

}
