package com.valuewith.tweaver.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageDto {
  private Long messageId;
  private Long groupMemberId;
  private String content;
}
