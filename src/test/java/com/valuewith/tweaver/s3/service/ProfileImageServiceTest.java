package com.valuewith.tweaver.s3.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

class ProfileImageServiceTest {

    @InjectMocks
    private ProfileImageService profileImageService;

    @Mock
    private AmazonS3 amazonS3;

    String cloudFrontDomain = "test-cloud-domain";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(profileImageService, "cloudFrontDomain", cloudFrontDomain);
    }

    @Test
    void uploadProfileImageSuccess() {
        MockMultipartFile file = new MockMultipartFile(
            "image", "test.jpeg", "image/jpeg", "image_content".getBytes()
        );

        when(amazonS3.putObject(any(PutObjectRequest.class)))
            .thenReturn(null);

        String result = profileImageService.uploadProfileImage(file);
        assertTrue(result.startsWith("https://"));
        assertTrue(result.contains(cloudFrontDomain));
        assertTrue(result.endsWith(file.getOriginalFilename().replace(" ", "_")));
    }
}