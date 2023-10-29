package com.valuewith.tweaver.group.dto;

import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.groupMember.dto.GroupMemberDto;
import com.valuewith.tweaver.place.dto.PlaceDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class TripGroupDto {
  private Long tripGroupId;
  private String name;
  private String content;
  private Integer maxUserNumber;
  private String tripArea;
  private LocalDate tripDate;
  private LocalDate dueDate;
  private String thumbnailUrl;
  private GroupStatus status;
  private LocalDateTime updateDateTime;
  private List<GroupMemberDto> groupMembers = new ArrayList<>();
  private List<PlaceDto> places = new ArrayList<>();
}
