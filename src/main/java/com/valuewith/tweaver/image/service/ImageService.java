package com.valuewith.tweaver.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${cloudfront.distribution.domain}")
    private String cloudFrontDomain;

    @Value("${s3.bucket.name}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    /**
     * 매개변수로 받는 MultipartFile을 S3에 업로드한 후 CloudFront URL을 리턴합니다. 반환된 URL은 USER Entity의 profileUrl에
     * 저장할 수 있습니다. 예: user.setProfileUrl(profileService.uploadProfileImage(file))
     * CustomException을 적용할 예정입니다.
     */
    public String uploadImageAndGetUrl(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("추가된 파일이 없습니다.");
        }

        if (!("image/png".equals(file.getContentType())
            || "image/jpeg".equals(file.getContentType())
            || "image/jpg".equals(file.getContentType()))) {
            throw new IllegalArgumentException("올바르지 않은 이미지 파일입니다. PNG, JPG, JPEG 형식만 가능합니다.");
        }

        String fileName;
        try {
            fileName = generateFileName(file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(
                new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
            );
            return UriComponentsBuilder
                .fromHttpUrl("https://" + cloudFrontDomain)
                .pathSegment(fileName)
                .toUriString();
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장소에 업로드를 실패했습니다.", e);
        }
    }

    /**
     * profileUrl 중복 방지를 위해 고유한 파일 이름을 생성해 리턴합니다.
     * 파일명이 한글 또는 기타 언어로 되어있을수 있어 encoding 방식으로 변경.
     */
    public String generateFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename().replace(" ", "_");
        String encodedFilename;
        encodedFilename = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8);
        return String.format(
            "profile/%s-%s", UUID.randomUUID().toString(), encodedFilename
        );
    }

    /**
     * 프로필 새 이미지를 S3에 업로드한 후 S3에 이미지가 있는지 확인,
     * 업로드 성공 -> 기존 이미지 S3 삭제
     * 업로드 실패 -> 기존 이미지 그대로 유지
     */
    public String modifiedImageWithFallback(MultipartFile newFile, String currentUrl) {
        String newImageUrl = uploadImageAndGetUrl(newFile);
        if (newImageUrl != null && !newImageUrl.isBlank()) {
            String currentKey = currentUrl
                .replace("https://" + cloudFrontDomain + "/", "")
                .replace("%2F", "/");
            amazonS3.deleteObject(bucketName, currentKey);
            return newImageUrl;
        } else {
            return currentUrl;
        }
    }
}
