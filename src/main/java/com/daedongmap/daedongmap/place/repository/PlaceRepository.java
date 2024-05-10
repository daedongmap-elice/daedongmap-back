package com.daedongmap.daedongmap.place.repository;

import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.review.domain.Review;
import com.daedongmap.daedongmap.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    public void deleteById(Long id);

    Optional<Place> findByKakaoPlaceId(Long kakaoPlaceId);

    @Query(value = "select  place.average_rating, place.x, place.y, place.id, place.kakao_place_id, place.address_name, place.category_name, place.phone, place.place_name, place.place_url, place.road_address_name, count(review.id) review_cnt, max(file_path) review_image_path " +
            "from place join review on place.kakao_place_id = review.kakao_place_id join (select review_id, max(file_path) file_path from review_image group by review_id) ri on ri.review_id = review.id " +
            " where place.x between :x1 and :x2 and place.y between :y1 and :y2 group by place.id order by rand() ", nativeQuery = true )
    public List<Place> findByReasonPlace(@Param("x1") Double x1, @Param("x2") Double x2, @Param("y1") Double y1, @Param("y2") Double y2);

    @Query(value = "select place.average_rating, place.x, place.y, place.id, place.kakao_place_id, place.address_name, place.category_name, place.phone, place.place_name, place.place_url, place.road_address_name, count(review.id) review_cnt, max(file_path) review_image_path " +
            "from place join review on place.kakao_place_id = review.kakao_place_id join (select review_id, max(file_path) file_path from review_image group by review_id) ri on ri.review_id = review.id " +
            "where place.x between :x1 and :x2 and place.y between :y1 and :y2 group by place.id ORDER BY place.average_rating desc", nativeQuery = true)
    public List<Place> findByReasonPlaceOrderByRate(@Param("x1") Double x1, @Param("x2") Double x2, @Param("y1") Double y1, @Param("y2") Double y2);

    @Query(value = "select place.average_rating, place.x, place.y, place.id, place.kakao_place_id, place.address_name, place.category_name, place.phone, place.place_name, place.place_url, place.road_address_name, count(review.id) review_cnt, max(file_path) review_image_path " +
            "from place join review on place.kakao_place_id = review.kakao_place_id join (select review_id, max(file_path) file_path from review_image group by review_id) ri on ri.review_id = review.id " +
            "where place.x between :x1 and :x2 and place.y between :y1 and :y2 group by place.id order by (6371*ACOS(COS(RADIANS(:y))*COS(RADIANS(place.y))*COS(radians(place.x)-RADIANS(:x))+SIN(RADIANS(:y))*SIN(RADIANS(place.y)))) asc", nativeQuery = true)
    public List<Place> findByReasonPlaceOrderByDistance(@Param("x") Double x, @Param("y") Double y, @Param("x1") Double x1, @Param("x2") Double x2, @Param("y1") Double y1, @Param("y2") Double y2);

    @Modifying
    @Query(value="update Place place set place.averageRating = (select round(avg(review.averageRating), 1) from Review review where kakaoPlaceId = :kakaoPlaceId) where place.kakaoPlaceId = :kakaoPlaceId")
    public void updatePlaceRateById(Long kakaoPlaceId);
}
