package com.valuewith.tweaver.defaultImage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DefaultImageResponseDto {
    private String locationName;
    private String imageUrl;

    public static DefaultImageResponseDto from(String locationName, String imageUrl) {
        return DefaultImageResponseDto.builder()
            .locationName(locationName)
            .imageUrl(imageUrl)
            .build();
    }
}
