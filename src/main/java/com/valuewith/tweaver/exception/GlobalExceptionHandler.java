package com.valuewith.tweaver.exception;

import com.valuewith.tweaver.defaultImage.exception.InvalidFileMediaTypeException;
import com.valuewith.tweaver.defaultImage.exception.LocationNameEmptyException;
import com.valuewith.tweaver.defaultImage.exception.NoFileProvidedException;
import com.valuewith.tweaver.defaultImage.exception.S3ImageNotFoundException;
import com.valuewith.tweaver.defaultImage.exception.UrlEmptyException;
import com.valuewith.tweaver.exception.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoFileProvidedException.class)
    public ResponseEntity<ErrorResponseDto> handleNoFileProvidedException(NoFileProvidedException e) {
        ErrorResponseDto responseDto = ErrorResponseDto.from(e.getErrorCode());
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(responseDto);
    }

    @ExceptionHandler(InvalidFileMediaTypeException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidFileMediaTypeException(InvalidFileMediaTypeException e) {
        ErrorResponseDto responseDto = ErrorResponseDto.from(e.getErrorCode());
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(responseDto);
    }

    @ExceptionHandler(S3ImageNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleS3ImageNotFoundException(S3ImageNotFoundException e) {
        ErrorResponseDto responseDto = ErrorResponseDto.from(e.getErrorCode());
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(responseDto);
    }

    @ExceptionHandler(UrlEmptyException.class)
    public ResponseEntity<ErrorResponseDto> handleUrlEmptyException(UrlEmptyException e) {
        ErrorResponseDto responseDto = ErrorResponseDto.from(e.getErrorCode());
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(responseDto);
    }

    @ExceptionHandler(LocationNameEmptyException.class)
    public ResponseEntity<ErrorResponseDto> handleLocationNameEmptyException(LocationNameEmptyException e) {
        ErrorResponseDto responseDto = ErrorResponseDto.from(e.getErrorCode());
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(responseDto);
    }
}
