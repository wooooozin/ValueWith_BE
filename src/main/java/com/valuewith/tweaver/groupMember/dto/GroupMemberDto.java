package com.valuewith.tweaver.groupMember.dto;

import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.constants.MemberRole;
import com.valuewith.tweaver.message.dto.MessageDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupMemberDto {
  private Long groupMemberId;
  private Long memberId;
  private Long tripGroupId;
  private Long chatRoomId;
  private MemberRole memberRole;
  private Boolean isBanned;
  private ApprovedStatus approvedStatus;
  private LocalDateTime approvedDateTime;
  private List<MessageDto> messages = new ArrayList<>();
}
