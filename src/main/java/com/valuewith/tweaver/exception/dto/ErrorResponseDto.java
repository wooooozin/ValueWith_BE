package com.valuewith.tweaver.exception.dto;

import com.valuewith.tweaver.constants.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponseDto {
    private HttpStatus status;
    private int statusCode;
    private String message;
    private ErrorCode errorCode;

    public static ErrorResponseDto from(ErrorCode errorCode) {
        return ErrorResponseDto.builder()
            .message(errorCode.getDescription())
            .status(errorCode.getHttpStatus())
            .statusCode(errorCode.getHttpStatus().value())
            .errorCode(errorCode)
            .build();
    }
}
