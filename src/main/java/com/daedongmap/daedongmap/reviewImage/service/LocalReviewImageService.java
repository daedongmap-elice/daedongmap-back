package com.daedongmap.daedongmap.reviewImage.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class LocalReviewImageService implements ReviewImageService{
    @Override
    public String uploadReviewImage(MultipartFile reviewImage) throws IOException {
        // 이미지 파일 이름과 경로 생성
        String fileName = UUID.randomUUID().toString();
        String filePath = "/images/" + fileName;

        // 로컬에 파일 저장
        File file = new File(filePath);
        reviewImage.transferTo(file);

        return filePath;
    }
}
