package com.daedongmap.daedongmap.place.repository;

import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    public void deleteById(Long id);

    Optional<Place> findByKakaoPlaceId(Long kakaoPlaceId);

    public List<Place> findByCategoryName(String categoryName);
}
