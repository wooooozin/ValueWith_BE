package com.valuewith.tweaver.alert.repository;

import com.valuewith.tweaver.alert.dto.AlertResponseDto;
import java.util.List;

public interface AlertDsl {
  List<AlertResponseDto> getAlertsByMemberId(Long memberId);
}
