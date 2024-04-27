package com.daedongmap.daedongmap.reviewImage.service;

import com.daedongmap.daedongmap.reviewImage.dto.ReviewImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReviewImageService {
    String uploadReviewImage(MultipartFile reviewImage, String fileName) throws IOException;
    List<ReviewImageDto> getReviewImage(Long reviewId);
    void deleteReviewImage(Long reviewId);
}
