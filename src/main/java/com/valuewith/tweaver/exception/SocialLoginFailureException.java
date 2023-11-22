package com.valuewith.tweaver.exception;

import com.valuewith.tweaver.constants.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SocialLoginFailureException extends RuntimeException{

  private final ErrorCode errorCode;
  private final String message;
  private final HttpStatus httpStatus;

  public SocialLoginFailureException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.message = errorCode.getDescription();
    this.httpStatus = errorCode.getHttpStatus();
  }
}
