package com.valuewith.tweaver.member.dto;

import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.constants.MemberRole;
import com.valuewith.tweaver.message.Message;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class MemberDto {
  private Long memberId;
  private Long userId;
  private Long groupId;
  private Long chatRoomId;
  private MemberRole memberRole;
  private Boolean isBanned;
  private ApprovedStatus approvedStatus;
  private LocalDateTime approvedDateTime;
  private List<Message> messages = new ArrayList<>();
}
