package com.daedongmap.daedongmap.reviewImage.service;


import com.daedongmap.daedongmap.reviewImage.repository.ReviewImageRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class LocalReviewImageServiceImpl extends ReviewImageService {

    public LocalReviewImageServiceImpl(ReviewImageRepository reviewImageRepository) {
        super(reviewImageRepository);
    }

    @Override
    public String uploadReviewImage(MultipartFile reviewImage, String fileName) throws IOException {
        return null;
    }

    @Override
    public void deleteReviewImage(Long reviewId) {

    }

}
