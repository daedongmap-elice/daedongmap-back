package com.daedongmap.daedongmap.place.service;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.place.domain.Place;
import com.daedongmap.daedongmap.place.dto.PlaceBasicInfoDto;
import com.daedongmap.daedongmap.place.dto.PlaceCreateDto;
import com.daedongmap.daedongmap.place.dto.PlaceUpdateDto;
import com.daedongmap.daedongmap.place.repository.PlaceRepository;
import com.daedongmap.daedongmap.review.repository.ReviewRepository;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public PlaceBasicInfoDto createPlace(PlaceCreateDto placeCreateDto) {
        placeRepository.findByKakaoPlaceId(placeCreateDto.getKakaoPlaceId()).ifPresent(e -> {
            throw new CustomException(ErrorCode.PLACE_KAKAO_ID_IN_USE);
        });
        StringTokenizer st = new StringTokenizer(placeCreateDto.getAddressName(), " ", true);

        Place place = Place.builder()
                .kakaoPlaceId(placeCreateDto.getKakaoPlaceId())
                .placeName(placeCreateDto.getPlaceName())
                .placeUrl(placeCreateDto.getPlaceUrl())
                .categoryName(placeCreateDto.getCategoryName())
                .addressName(placeCreateDto.getAddressName())
                .roadAddressName(placeCreateDto.getRoadAddressName())
                .phone(placeCreateDto.getPhone())
                .x(placeCreateDto.getX())
                .y(placeCreateDto.getY())
                .build();

        Place createPlace = placeRepository.save(place);
        return toPlaceBasicInfoDto(createPlace);
    }

    @Transactional(readOnly = true)
    public PlaceBasicInfoDto findPlaceById(Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));
        return toPlaceBasicInfoDto(place);
    }

    @Transactional
    public PlaceBasicInfoDto updatePlace(Long placeId, PlaceUpdateDto placeUpdateDto) {
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));
        place.updatePlace(placeUpdateDto);
        return toPlaceBasicInfoDto(place);
    }

    @Transactional
    public void deletePlace(Long placeId) {
        placeRepository.deleteById(placeId);
    }

    private PlaceBasicInfoDto toPlaceBasicInfoDto(Place place) {
        return PlaceBasicInfoDto.builder()
                .id(place.getId())
                .kakaoPlaceId(place.getKakaoPlaceId())
                .placeName(place.getPlaceName())
                .placeUrl(place.getPlaceUrl())
                .categoryName(place.getCategoryName())
                .addressName(place.getAddressName())
                .roadAddressName(place.getRoadAddressName())
                .x(place.getX())
                .y(place.getY())
                .build();
    }

    public List<PlaceBasicInfoDto> findReasonPlace(String filter, Double x1, Double x2, Double y1, Double y2, Double x, Double y) {
        List<Place> places;
        if (filter.equals("rating")) {
            places = placeRepository.findByReasonPlaceOrderByRate(x1, x2, y1, y2);
        } else if(filter.equals("distance")) {
            places = placeRepository.findByReasonPlaceOrderByDistance(x, y, x1, x2, y1, y2);
        } else {
            places = placeRepository.findByReasonPlace(x1, x2, y1, y2);
        }
        return places.stream()
                .map(this::toPlaceBasicInfoDto)
                .collect(Collectors.toList());
    }
}
