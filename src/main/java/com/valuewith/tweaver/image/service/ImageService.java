package com.valuewith.tweaver.image.service;


import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImageAndGetUrl(MultipartFile file);
    String generateFileName(MultipartFile file);
}
