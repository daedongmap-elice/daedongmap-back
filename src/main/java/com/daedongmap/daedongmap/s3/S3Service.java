package com.daedongmap.daedongmap.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile multipartFile) {
        log.info("uploadImage 실행");
        String s3FileName = createS3FileName(multipartFile.getOriginalFilename());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setContentType(multipartFile.getContentType());

            amazonS3.putObject(new PutObjectRequest(bucket + "/review", s3FileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.FAILED_FILE_UPLOAD);
        }

        return amazonS3.getUrl(bucket + "/review", s3FileName).toString();
    }

    public void deleteImage(String filePath) {
        log.info("deleteImage 실행");
        try {
            amazonS3.deleteObject(bucket + "/review", filePath);
            log.info("파일 삭제 성공: " + filePath);
        } catch (Exception e) {
            log.error("파일 삭제 실패: " + filePath, e);
            throw new CustomException(ErrorCode.FAILED_FILE_DELETE);
        }
    }

    private String createS3FileName(String fileName) {
        return UUID.randomUUID() + "_" + fileName;
    }

}
