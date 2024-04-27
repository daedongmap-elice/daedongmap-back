package com.daedongmap.daedongmap.reviewImage.service;

import com.daedongmap.daedongmap.reviewImage.dto.ReviewImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class LocalReviewImageService implements ReviewImageService{
    @Override
    public String uploadReviewImage(MultipartFile reviewImage, String fileName) throws IOException {
        // 이미지 파일 이름과 경로 생성
        return "/images/" + fileName;
    }

    @Override
    public List<ReviewImageDto> getReviewImage(Long reviewId) {
        return null;
    }

    @Override
    public void deleteReviewImage(Long reviewId) {

    }

}
