package com.daedongmap.daedongmap.review.repository;

import com.daedongmap.daedongmap.common.model.Category;
import com.daedongmap.daedongmap.common.model.Region;
import com.daedongmap.daedongmap.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUserId(Long userId);
    // todo: kakaoPlaceId로 바꾸기
    List<Review> findAllByPlaceId(Long placeId);
    List<Review> findAllByPlaceAddressNameContainingAndPlaceCategoryName(String addressName, String category);
}
