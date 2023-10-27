package com.valuewith.tweaver.group.dto;

import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.member.dto.MemberDto;
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
public class GroupDto {
  private Long groupId;
  private String name;
  private String content;
  private Integer maxUserNumber;
  private String tripArea;
  private LocalDate tripDate;
  private LocalDate dueDate;
  private String thumbnailUrl;
  private GroupStatus status;
  private LocalDateTime updateDateTime;
  private List<MemberDto> members = new ArrayList<>();
  private List<PlaceDto> places = new ArrayList<>();
}
