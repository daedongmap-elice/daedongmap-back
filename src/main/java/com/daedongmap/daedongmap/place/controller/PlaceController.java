package com.daedongmap.daedongmap.place.controller;

import com.daedongmap.daedongmap.place.dto.PlaceBasicInfoDto;
import com.daedongmap.daedongmap.place.dto.PlaceCreateDto;
import com.daedongmap.daedongmap.place.dto.PlaceUpdateDto;
import com.daedongmap.daedongmap.place.service.PlaceService;
import com.daedongmap.daedongmap.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final ReviewService reviewService;

    @PostMapping()
    @Operation(summary = "음식점 등록", description = "해당 음식점의 첫 리뷰를 등록할때 저장되는 음식점 정보")
    public ResponseEntity<PlaceBasicInfoDto> createPlace(@RequestBody PlaceCreateDto placeCreateDto) {
        PlaceBasicInfoDto createdPlaceDto = placeService.createPlace(placeCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlaceDto);
    }

    @GetMapping("/region")
    @Operation(summary = "음식점 지역 검색", description = "검색한 지역의 음식점을 모두 조회합니다.")
    public ResponseEntity<List<PlaceBasicInfoDto>> getRegionPlace(@RequestParam(defaultValue="recommend") String filter, @RequestParam Double x1, @RequestParam Double x2, @RequestParam Double y1, @RequestParam Double y2, @RequestParam Double x, @RequestParam Double y) {
        List<PlaceBasicInfoDto> findPlace = placeService.findReasonPlace(filter, x1, x2, y1, y2, x, y);
        return ResponseEntity.status(HttpStatus.OK).body(findPlace);
    }

    @GetMapping("/{placeId}")
    @Operation(summary = "음식점 상세조회", description = "음식점의 상세 정보")
    public ResponseEntity<PlaceBasicInfoDto> getDetailPlace(@PathVariable Long placeId) {
        PlaceBasicInfoDto findPlaceDto = placeService.findPlaceById(placeId);
        findPlaceDto.setReviewImagePath(reviewService.findReviewByKakaoPlaceIdDesc(findPlaceDto.getKakaoPlaceId()));
        return ResponseEntity.status(HttpStatus.OK).body(findPlaceDto);
    }

    @PutMapping("/{placeId}")
    @Operation(summary = "음식점 상세수정", description = "음식점의 상세 정보 수정")
    public ResponseEntity<PlaceBasicInfoDto> modifyPlace(@PathVariable Long placeId, @RequestBody PlaceUpdateDto placeUpdateDto) {
        PlaceBasicInfoDto updatedPlaceDto = placeService.updatePlace(placeId, placeUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPlaceDto);
    }

    @DeleteMapping("/{placeId}")
    @Operation(summary = "음식점 삭제", description = "해당 placeId 음식점 삭제")
    public void deletePlace(@PathVariable Long placeId) {
        placeService.deletePlace(placeId);
    }
}
