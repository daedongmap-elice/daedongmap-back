package com.daedongmap.daedongmap.place.repository;

import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    public void deleteById(Long id);

    Optional<Place> findByKakaoPlaceId(Long kakaoPlaceId);

    @Query(value = "select place " +
            "from Place place " +
            "where place.x between :x1 and :x2 and place.y between :y1 and :y2")
    public List<Place> findByReasonPlace(@Param("x1") Double x1, @Param("x2") Double x2, @Param("y1") Double y1, @Param("y2") Double y2);

    @Query(value = "select place " +
            "from Place place " +
            "where place.x between :x1 and :x2 and place.y between :y1 and :y2 order by averageRating desc")
    public List<Place> findByReasonPlaceOrderByRate(@Param("x1") Double x1, @Param("x2") Double x2, @Param("y1") Double y1, @Param("y2") Double y2);
}
