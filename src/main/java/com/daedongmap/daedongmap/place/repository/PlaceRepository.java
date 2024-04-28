package com.daedongmap.daedongmap.place.repository;

import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    public void deleteById(Long id);
}
