package com.daedongmap.daedongmap.reviewImage.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.daedongmap.daedongmap.reviewImage.dto.ReviewImageDto;
import com.daedongmap.daedongmap.reviewImage.model.ReviewImage;
import com.daedongmap.daedongmap.reviewImage.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ReviewImageService implements ReviewImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;
    private final ReviewImageRepository reviewImageRepository;

    @Override
    public String uploadReviewImage(MultipartFile reviewImage, String fileName) throws IOException {
//        String fileName = reviewImage.getOriginalFilename() + "_" + UUID.randomUUID();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(reviewImage.getSize());
        metadata.setContentType(reviewImage.getContentType());

        amazonS3Client.putObject(bucket, fileName, reviewImage.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    @Override
    public List<ReviewImageDto> getReviewImage(Long reviewId) {
        List<ReviewImage> reviewImageList = reviewImageRepository.findAllByReviewId(reviewId);
        return reviewImageList.stream()
                .map(ReviewImageDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReviewImage(Long reviewId) {
        // s3 에서 이미지 파일 삭제
        List<ReviewImage> reviewImageList = reviewImageRepository.findAllByReviewId(reviewId);
        for (ReviewImage reviewImage : reviewImageList) {
            amazonS3Client.deleteObject(bucket, reviewImage.getFileName());
        }

        // DB 에서 이미지 파일 정보 삭제
        reviewImageRepository.deleteByReviewId(reviewId);
    }

}
