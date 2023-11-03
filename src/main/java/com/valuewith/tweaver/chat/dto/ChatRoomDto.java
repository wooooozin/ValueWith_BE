package com.valuewith.tweaver.chat.dto;

import com.valuewith.tweaver.message.dto.MessageDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomDto {
  private Long chatRoomId;
  private Long tripGroupId;
  private String title;
  private List<MessageDto> messages = new ArrayList<>();
}
