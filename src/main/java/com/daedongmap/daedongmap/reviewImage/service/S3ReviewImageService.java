package com.daedongmap.daedongmap.reviewImage.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ReviewImageService implements ReviewImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    @Override
    public String uploadReviewImage(MultipartFile reviewImage) throws IOException {
        String fileName = reviewImage.getOriginalFilename() + "_" + UUID.randomUUID();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(reviewImage.getSize());
        metadata.setContentType(reviewImage.getContentType());

        amazonS3Client.putObject(bucket, fileName, reviewImage.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

}
