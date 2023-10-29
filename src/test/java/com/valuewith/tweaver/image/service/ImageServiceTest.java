package com.valuewith.tweaver.image.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.valuewith.tweaver.constants.ImageType;
import com.valuewith.tweaver.defaultImage.exception.InvalidFileMediaTypeException;
import com.valuewith.tweaver.defaultImage.exception.NoFileProvidedException;
import com.valuewith.tweaver.defaultImage.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private AmazonS3 amazonS3;

    private final String cloudFrontDomain = "test-cloud-domain";
    private final String bucketName = "test-bucket";


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(imageService, "cloudFrontDomain", cloudFrontDomain);
        ReflectionTestUtils.setField(imageService, "bucketName", bucketName);
    }

    @Test
    void uploadProfileImageSuccess() {
        MockMultipartFile file = new MockMultipartFile(
            "image", "test.jpeg", "image/jpeg", "image_content".getBytes()
        );

        when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());
        when(amazonS3.doesObjectExist(eq(bucketName), anyString())).thenReturn(true);


        String result = imageService.uploadImageAndGetUrl(file, ImageType.PROFILE);

        assertNotNull(result);
        assertTrue(result.startsWith("https://"));
        assertTrue(result.contains(cloudFrontDomain));
        assertTrue(result.endsWith(file.getOriginalFilename().replace(" ", "_")));
    }

    @Test
    void uploadProfileImageFailureInvalidFile() {
        MockMultipartFile file = new MockMultipartFile(
            "image", "test.txt", "text/plain", "text_content".getBytes()
        );

        assertThrows(InvalidFileMediaTypeException.class, () ->
            imageService.uploadImageAndGetUrl(file, ImageType.PROFILE));
    }

    @Test
    void uploadProfileImageFailureEmptyFile() {
        MockMultipartFile file = new MockMultipartFile(
            "image", "", "image/jpeg", "".getBytes()
        );

        assertThrows(NoFileProvidedException.class, () ->
            imageService.uploadImageAndGetUrl(file, ImageType.PROFILE));
    }
}