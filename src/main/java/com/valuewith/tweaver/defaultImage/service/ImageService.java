package com.valuewith.tweaver.defaultImage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.valuewith.tweaver.constants.ErrorCode;
import com.valuewith.tweaver.constants.ImageType;
import com.valuewith.tweaver.defaultImage.entity.DefaultImage;
import com.valuewith.tweaver.defaultImage.exception.InvalidFileMediaTypeException;
import com.valuewith.tweaver.defaultImage.exception.LocationNameEmptyException;
import com.valuewith.tweaver.defaultImage.exception.NoFileProvidedException;
import com.valuewith.tweaver.defaultImage.exception.S3ImageNotFoundException;
import com.valuewith.tweaver.defaultImage.exception.UrlEmptyException;
import com.valuewith.tweaver.defaultImage.repository.DefaultImageRepository;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${cloudfront.distribution.domain}")
    private String cloudFrontDomain;

    @Value("${s3.bucket.name}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    private final DefaultImageRepository defaultImageRepository;

    /**
     * 매개변수로 받는 MultipartFile을 S3에 업로드한 후 CloudFront URL을 리턴합니다.
     * 반환된 URL은 ImageType에 맞는 엔티티에 저장되도록 매개변수를 넣어주시면 됩니다.
     * 그럼 S3 버켓에서 폴더 별로 분류되어 저장됩니다.
     *  PROFILE("profile/")
     *  THUMBNAIL("thumbnail/")
     *  LOCATION("location/")
     *  예: user.setProfileUrl(imageService.uploadImageAndGetUrl(file,ImageType.PROFILE))
     *  예: location.setThumbnailUrl(imageService.uploadImageAndGetUrl(file, ImageType.THUMBNAIL))
     */
    public String uploadImageAndGetUrl(MultipartFile file, ImageType imageType) {
        if (file.isEmpty()) {
            throw new NoFileProvidedException(ErrorCode.NO_FILE_UPLOAD);
        }

        if (!("image/png".equals(file.getContentType())
            || "image/jpeg".equals(file.getContentType())
            || "image/jpg".equals(file.getContentType()))) {
            throw new InvalidFileMediaTypeException(ErrorCode.INVALID_FILE_MEDIA_TYPE);
        }

        String fileName;
        try {
            fileName = generateFileName(file, imageType);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(
                new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
            );

            if (!amazonS3.doesObjectExist(bucketName, fileName)) {
                throw new S3ImageNotFoundException(ErrorCode.S3_IMAGE_NOT_FOUND);
            }
            return UriComponentsBuilder
                .fromHttpUrl("https://" + cloudFrontDomain)
                .pathSegment(fileName)
                .toUriString();
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장소에 업로드를 실패했습니다.", e);
        }
    }

    /**
     * profileUrl 중복 방지를 위해 고유한 파일 이름을 생성해 리턴합니다. 파일명이 한글 또는 기타 언어로 되어있을수 있어 encoding 방식으로 변경.
     */
    public String generateFileName(MultipartFile file, ImageType imageType) {
        String originalFileName = file.getOriginalFilename().replace(" ", "_");
        String encodedFilename;
        encodedFilename = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8);
        return String.format(
            "%s%s-%s", imageType.getPath(), UUID.randomUUID().toString(), encodedFilename
        );
    }

    /**
     * 프로필 새 이미지를 S3에 업로드한 후 S3에 이미지가 있는지 확인,
     * 업로드 이미지와 동일하게 엔티티 유형에 따른 ImageType 매개변수로 전달해주시면 됩니다.
     * 그래야 같은 폴더안에 이미지를 찾아 삭제할 수 있습니다.
     * 업로드 성공 -> 기존 이미지 S3 삭제 업로드
     * 실패 -> 기존 이미지 그대로 유지 및 에러 발생
     */
    public String modifiedImageWithFallback(MultipartFile newFile, String currentUrl, ImageType imageType) {
        if (currentUrl == null || currentUrl.isBlank()) {
            throw new UrlEmptyException(ErrorCode.URL_IS_EMPTY);
        }

        String newImageUrl = uploadImageAndGetUrl(newFile, imageType);

        if (newImageUrl.isBlank()) {
            throw new NoFileProvidedException(ErrorCode.NO_FILE_UPLOAD);
        }

        try {
            String currentKey = generateKey(currentUrl);
            if (!amazonS3.doesObjectExist(bucketName, currentKey)) {
                throw new S3ImageNotFoundException(ErrorCode.S3_IMAGE_NOT_FOUND);
            }

            boolean exists = defaultImageRepository.existsDefaultImageByImageName(currentUrl);
            if(!exists) {
                amazonS3.deleteObject(bucketName, currentKey);
            }

            return newImageUrl;
        } catch (Exception e) {
            String newKey = generateKey(newImageUrl);
            amazonS3.deleteObject(bucketName, newKey);
            throw e;
        }
    }

    /**
     * S3 저장소의 원본 파일 이름으로 변경해주는 메서드입니다.
     */
    public String generateKey(String url) {
        return url
            .replace("https://" + cloudFrontDomain + "/", "")
            .replace("%2F", "/");
    }

    public String randomDefaultImageUploadAndGetUrl(MultipartFile file, ImageType imageType, String name) {
        if (name == null || name.isBlank()) {
            throw new LocationNameEmptyException(ErrorCode.LOCATION_NAME_NOT_FOUNT);
        }
        String imageUrl = uploadImageAndGetUrl(file, imageType);
        DefaultImage defaultImage = DefaultImage.builder()
            .imageName(name)
            .defaultImageUrl(imageUrl)
            .createdDateTime(LocalDateTime.now())
            .build();

        defaultImageRepository.save(defaultImage);
        return imageUrl;
    }
}
