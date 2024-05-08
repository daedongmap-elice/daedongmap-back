package com.daedongmap.daedongmap.review.repository;

import com.daedongmap.daedongmap.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUserId(Long userId);
    List<Review> findAllByPlace_KakaoPlaceId(Long kakaoPlaceId);
    List<Review> findAllByPlace_CategoryName(String categoryName);
    List<Review> findAllByPlace_AddressNameContaining(String region);
    List<Review> findAllByPlace_CategoryNameAndPlace_AddressNameContaining(String categoryName, String region);
    @Query(value = "select review " +
            "from Review review " +
            "where place.kakaoPlaceId = :kakaoPlaceId order by review.createdAt limit 1")
    Review findFirstByKakaoPlaceIdOrderByCreatedAtDesc(@Param("kakaoPlaceId") Long kakaoPlaceId);
}
