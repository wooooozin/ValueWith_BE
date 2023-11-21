package com.valuewith.tweaver.component;

import com.valuewith.tweaver.alert.dto.AlertRequestDto;
import com.valuewith.tweaver.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AlertListener {
  private final AlertService alertService;
  @TransactionalEventListener
  @Async // 비동기적으로 처리
  public void alarmHandler(AlertRequestDto form) {
    alertService.send(form);
  }
}
