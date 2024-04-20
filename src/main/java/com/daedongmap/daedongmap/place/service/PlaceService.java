package com.daedongmap.daedongmap.place.service;

import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.place.dto.PlaceCreateDto;
import com.daedongmap.daedongmap.place.dto.PlaceUpdateDto;
import com.daedongmap.daedongmap.place.repository.PlaceRepository;
import com.daedongmap.daedongmap.review.dto.ReviewBasicInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    @Transactional
    public void createPlace(PlaceCreateDto placeCreateDto) {

    }

    @Transactional(readOnly = true)
    public void findReviewById(Long reviewId) {

    }

    @Transactional
    public void updatePlace(Long placeId, PlaceUpdateDto placeUpdateDto) {
    }

    @Transactional
    public void deletePlace(Long reviewId) {
        placeRepository.deleteById(reviewId);
    }
}
