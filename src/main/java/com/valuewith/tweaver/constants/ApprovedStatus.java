package com.valuewith.tweaver.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApprovedStatus {
  PENDING("대기"),
  APPROVED("승인"),
  REJECTED("거절");

  private final String description;
}
