package com.daedongmap.daedongmap.place.repository;

import com.daedongmap.daedongmap.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Review, Long> {

    public void deleteById(Long id);
}
