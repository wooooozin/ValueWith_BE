package com.valuewith.tweaver.chat.dto;

import com.valuewith.tweaver.message.dto.MessageDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class ChatRoomDto {
  private Long chatRoomId;
  private Long groupId;
  private String title;
  private List<MessageDto> messages = new ArrayList<>();
}
