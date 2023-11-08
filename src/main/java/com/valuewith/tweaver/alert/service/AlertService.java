package com.valuewith.tweaver.alert.service;

import com.valuewith.tweaver.alert.dto.AlertRequestDto;
import com.valuewith.tweaver.alert.dto.AlertResponseDto;
import com.valuewith.tweaver.alert.entity.Alert;
import com.valuewith.tweaver.alert.repository.AlertRepository;
import com.valuewith.tweaver.alert.repository.EmitterRepository;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AlertService {
  private static final Long DEFAULT_TIMEOUT = 120L * 1000 * 60; // SSE 유효시간
  private final EmitterRepository emitterRepository;
  private final AlertRepository alertRepository;
  private final TripGroupRepository tripGroupRepository;

  public SseEmitter subscribe(Long memberId, String lastEventId) {

    String emitterId = memberId + "_" + System.currentTimeMillis(); // 데이터 유실 시점 파악 위함

    // 클라이언트의 sse 연결 요청에 응답하기 위한 SseEmitter 객체 생성
    // 유효시간 지정으로 시간이 지나면 클라이언트에서 자동으로 재연결 요청함
    SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

    // SseEmitter 의 완료/시간초과/에러로 인한 전송 불가 시 sseEmitter 삭제
    emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
    emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
    emitter.onError((e) -> emitterRepository.deleteById(emitterId));

    // 연결 직후, 데이터 전송이 없을 시 503 에러 발생. 503 에러를 방지하기 위한 더미 이벤트 전송
    sendAlert(emitter, emitterId, emitterId, "EventStream Created. [memberId=" + memberId + "]");

    // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
    if (!lastEventId.isEmpty()) { // 클라이언트가 미수신한 Event 유실 예방, 연결이 끊켰거나 미수신된 데이터를 다 찾아서 보내준다.
      Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartById(String.valueOf(memberId));
      eventCaches.entrySet().stream()
          .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
          .forEach(entry -> sendAlert(emitter, emitterId, entry.getKey(), entry.getValue()));
    }

    return emitter;
  }

  @Transactional
  // 알림 보낼 로직에 send 메서드 호출하면 됨
  public void send(AlertRequestDto alertRequestDto) {
    Alert saveAlert = alertRepository.save(Alert.from(alertRequestDto));

    // 받을 사람 id
    Long memberId = alertRequestDto.getMember().getMemberId();
    String eventId = memberId + "_" + System.currentTimeMillis(); // 데이터 유실 시점 파악 위함

    // 유저의 모든 SseEmitter 가져옴
    Map<String, SseEmitter> emitters = emitterRepository.findAllStartById(memberId + "_");
    emitters.forEach(
        (key, emitter) -> {
          emitterRepository.saveEventCache(key, saveAlert.getAlertId());
          sendAlert(emitter,
              eventId,
              key,
              AlertResponseDto.from(
                  saveAlert,
                  tripGroupRepository.findById(saveAlert.getGroupId()).get().getName()));
          sendAlertCount(emitter,eventId, key, getAlertCount(memberId));
        }
    );
  }

  // 클라이언트에게 알림 전달하는 부분
  private void sendAlert(SseEmitter emitter, String eventId, String emitterId,
      Object data) {
    try {
      emitter.send(SseEmitter.event()
          .id(eventId)
          .name("sse")
          .data(data));
      log.info("알림 전송 완료");
    } catch (IOException exception) {
      emitterRepository.deleteById(emitterId);
      log.error("SSE 연결이 올바르지 않습니다. 해당 memberId={}", eventId);
    }
  }

  /**
   * 알림을 읽거나, 삭제하거나, 실시간으로 받았을 때 클라이언트에게 알림갯수
   */
  private void sendAlertCount(SseEmitter emitter, String eventId, String emitterId,
      Object data) {
    try {
      emitter.send(SseEmitter.event()
          .id(eventId)
          .name("alertCount")
          .data(data));
      log.info("알림 갯수 전송 완료");
    } catch (IOException exception) {
      emitterRepository.deleteById(emitterId);
      log.error("SSE 연결이 올바르지 않습니다. 해당 memberId={}", eventId);
    }
  }

  // 알람 조회
  public List<AlertResponseDto> getAlerts(Long memberId) {
    return alertRepository.getAlertsByMemberId(memberId);
  }

  // 읽은 알람 isChecked true로 설정
  public Long check(Long memberId, Long alertId) {
    Alert alert = alertRepository.findById(alertId)
        .orElseThrow(() -> new RuntimeException("확인하고자 하는 알람이 존재하지 않습니다."));
    if (Objects.equals(memberId, alert.getMember().getMemberId())) {
      alert.checkAlert();
    }
    return getAlertCount(memberId);
  }

  // 삭제한 알람은 삭제시키기
  public Long delete(Long memberId, Long alertId) {
    Alert alert = alertRepository.findByAlertIdAndIsDeleted(alertId, false)
        .orElseThrow(() -> new RuntimeException("삭제하고자 하는 알람이 존재하지 않습니다."));
    if (Objects.equals(memberId, alert.getMember().getMemberId())) {
      alertRepository.delete(alert);
    }
    return getAlertCount(memberId);
  }

  public Long getAlertCount(Long memberId) {
    return alertRepository.getAlertCountByMemberId(memberId);
  }

}
