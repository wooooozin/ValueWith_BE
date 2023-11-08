package com.valuewith.tweaver.group.dto;

import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.place.dto.PlaceDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripGroupRequestDto {
  private Long tripGroupId;
  private String name;
  private String content;
  private Integer maxMemberNumber;
  private Integer currentMemberNumber;
  private String tripArea;
  private LocalDate tripDate;
  private LocalDate dueDate;
  private String thumbnailUrl;
  private GroupStatus status;
  private List<PlaceDto> places = new ArrayList<>();

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }
}
