package com.valuewith.tweaver.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NO_FILE_UPLOAD("추가된 파일이 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_FILE_MEDIA_TYPE("JPG, JPEG, PNG 형식만 가능합니다.", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    S3_IMAGE_NOT_FOUND("이미지 저장소에서 파일을 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    URL_IS_EMPTY("제공된 URL이 없습니다.", HttpStatus.BAD_REQUEST),
    LOCATION_NAME_NOT_FOUNT("지역 이름은 필수입니다.", HttpStatus.BAD_REQUEST)
    ;


    private final String description;
    private final HttpStatus httpStatus;

}
