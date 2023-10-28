package com.valuewith.tweaver.image.controller;

import com.valuewith.tweaver.constants.ImageType;
import com.valuewith.tweaver.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/random-location")
public class LocationImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadLocationImage(
        @RequestParam("file")MultipartFile file
    ) {
        log.info(file.getOriginalFilename());
        try {
            String url = imageService.uploadImageAndGetUrl(file, ImageType.LOCATION);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Image upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> modifyLocationImage(
        @RequestParam("file") MultipartFile newFile,
        @RequestParam("url") String currentUrl
    ) {
        try {
            String url = imageService.modifiedImageWithFallback(newFile, currentUrl, ImageType.LOCATION);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Image modification failed: " + e.getMessage());
        }
    }
}
