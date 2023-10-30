package com.valuewith.tweaver.defaultImage.controller;

import com.valuewith.tweaver.constants.ImageType;
import com.valuewith.tweaver.defaultImage.dto.DefaultImageResponseDto;
import com.valuewith.tweaver.defaultImage.service.ImageService;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/images")
public class LocationImageController {

    private final ImageService imageService;

    @ApiOperation(value = "로케이션 이미지 업로드", notes = "S3 이미지 업로드 테스트 POST API")
    @PostMapping("/test-upload")
    public ResponseEntity<String> uploadLocationImage(
        @RequestParam("file") MultipartFile file
    ) {
        String url = imageService.uploadImageAndGetUrl(file, ImageType.LOCATION);
        return ResponseEntity.ok(url);
    }

    @ApiOperation(value = "로케이션 이미지 수정", notes = "S3 이미지 수정하는 테스트 POST API")
    @PostMapping("/test-update")
    public ResponseEntity<String> modifyLocationImage(
        @RequestParam("file") MultipartFile newFile,
        @RequestParam("url") String currentUrl
    ) {
        String url = imageService.modifiedImageWithFallback(newFile, currentUrl, ImageType.LOCATION);
        return ResponseEntity.ok(url);
    }

    @ApiOperation(
        value = "로케이션 디폴트 이미지 업로드 API",
        notes = "디폴트 지역 이미지 추가 api 입니다. name에는 지역명을 입력해주세요"
    )
    @PostMapping("/upload-location")
    public ResponseEntity<DefaultImageResponseDto> createLocationImage(
        @RequestParam("file") MultipartFile file,
        @RequestParam("name") String locationName
    ) {
        String imageUrl = imageService.randomDefaultImageUploadAndGetUrl(file, ImageType.LOCATION, locationName);
        DefaultImageResponseDto responseDto = DefaultImageResponseDto.from(locationName, imageUrl);
        return ResponseEntity.ok(responseDto);
    }

    @ApiOperation(
        value = "멤버 디폴트 이미지 업로드 API",
        notes = "디폴트 멤버 이미지 추가 api 입니다. name에는 '멤버' 를 입력해주세요. "
            + "프로필 이미지 미 설정시 defaultImage repository에서 '멤버'로 find해서 url 넘겨주면 됩니다."
    )
    @PostMapping("/upload-member")
    public ResponseEntity<DefaultImageResponseDto> createMemberImage(
        @RequestParam("file") MultipartFile file,
        @RequestParam("name") String memberName
    ) {
        String imageUrl = imageService.randomDefaultImageUploadAndGetUrl(file, ImageType.MEMBER, memberName);
        DefaultImageResponseDto responseDto = DefaultImageResponseDto.from(memberName, imageUrl);
        return ResponseEntity.ok(responseDto);
    }
}
