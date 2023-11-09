package com.valuewith.tweaver.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Provider {
  NORMAL("NORMAL"),
  KAKAO("KAKAO");

  private final String value;
}