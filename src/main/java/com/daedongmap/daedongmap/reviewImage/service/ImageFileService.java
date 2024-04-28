package com.daedongmap.daedongmap.reviewImage.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageFileService {
    String uploadReviewImage(MultipartFile reviewImage, String fileName) throws IOException;
    void deleteReviewImage(Long reviewId);
}
