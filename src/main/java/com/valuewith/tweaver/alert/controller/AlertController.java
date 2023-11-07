package com.valuewith.tweaver.alert.controller;

import com.valuewith.tweaver.alert.dto.AlertResponseDto;
import com.valuewith.tweaver.alert.service.AlertService;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/alert/*")
@RequiredArgsConstructor
public class AlertController {
  private final AlertService alertService;
  // TODO: 로그인 기능 pull받고 로그인한 유저객체 가져와서 사용하면 이부분 삭제
  private final MemberRepository memberRepository;

  /**
   * TODO: {id를}토큰으로 변경
   * 로그인 한 유저 알람 sse 연결
   */
  @GetMapping(value = "/subscribe/{id}", produces = "text/event-stream")
  public ResponseEntity<SseEmitter> subscribe(@PathVariable Long id,
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

    // 서비스를 통해 생성된 SseEmitter를 반환
    return ResponseEntity.ok(alertService.subscribe(id, lastEventId));
  }

  // 내 알림 목록 조회
  @GetMapping
  public ResponseEntity<List<AlertResponseDto>> alerts() {
    //TODO: 로그인한 유저의 값을 가져오기(현재, 임시로 넣어둔 1L 객체 가져오기)
    Member member = memberRepository.findById(1L).get();

    return ResponseEntity.ok(alertService.getAlerts(member.getMemberId()));
  }

  /**
   * 알림 읽음 처리
   * @param alarmId
   */
  @PatchMapping("/{alertId}")
  public ResponseEntity<Long> check(
      @PathVariable("alertId") Long alarmId
  ) {
    //TODO: 로그인한 유저의 값을 가져오기(현재, 임시로 넣어둔 1L 객체 가져오기)
    Member member = memberRepository.findById(1L).get();
    Long alertCnt = alertService.check(member.getMemberId(), alarmId);
    return ResponseEntity.ok(alertCnt);
  }

  /**
   * 알림 삭제 처리
   * @param alarmId
   */
  @DeleteMapping("/{alertId}")
  public ResponseEntity<Long> delete(
      @PathVariable("alertId") Long alarmId
  ) {
    //TODO: 로그인한 유저의 값을 가져오기(현재, 임시로 넣어둔 1L 객체 가져오기)
    Member member = memberRepository.findById(1L).get();
    Long alertCnt = alertService.delete(member.getMemberId(), alarmId);
    return ResponseEntity.ok(alertCnt);
  }

}
