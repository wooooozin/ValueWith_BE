package com.valuewith.tweaver.message.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class MessageDto {
  private Long messageId;
  private Long groupMemberId;
  private String content;
}
