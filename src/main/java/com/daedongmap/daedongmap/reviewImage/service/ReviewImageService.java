package com.daedongmap.daedongmap.reviewImage.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ReviewImageService {
    String uploadReviewImage(MultipartFile reviewImage) throws IOException;
}
