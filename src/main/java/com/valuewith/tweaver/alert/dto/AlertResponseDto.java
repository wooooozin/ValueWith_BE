package com.valuewith.tweaver.alert.dto;

import com.valuewith.tweaver.alert.entity.Alert;
import com.valuewith.tweaver.constants.AlertContent;
import com.valuewith.tweaver.constants.RedirectUrlType;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertResponseDto {
  private Long alertId;
  private String redirectUrl;
  private AlertContent content;
  private LocalDateTime createdDateTime;
  private Long groupId;
  private String groupName;

  public static AlertResponseDto from(Alert alert, String groupName) {
    return AlertResponseDto.builder()
        .alertId(alert.getAlertId())
        .content(alert.getContent())
        .groupId(alert.getGroupId())
        .createdDateTime(alert.getCreatedDateTime())
        .redirectUrl(alert.getRedirectUrl())
        .groupName(groupName)
        .build();
  }
}
