package com.valuewith.tweaver.alert.controller;

import com.valuewith.tweaver.alert.dto.AlertResponseDto;
import com.valuewith.tweaver.alert.service.AlertService;
import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import com.valuewith.tweaver.member.service.MemberService;
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
@RequestMapping("/alert")
@RequiredArgsConstructor
public class AlertController {
  private final MemberService memberService;
  private final AlertService alertService;
  private final TokenService tokenService;

  /**
   * 로그인 한 유저 알람 sse 연결
   */
  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public ResponseEntity<SseEmitter> subscribe(
      @RequestHeader("Authorization") String token,
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
    Member member = memberService.findMemberByEmail(tokenService.getMemberEmail(token));
    // 서비스를 통해 생성된 SseEmitter를 반환
    return ResponseEntity.ok(alertService.subscribe(member.getMemberId(), lastEventId));
  }

  // 내 알림 목록 조회
  @GetMapping
  public ResponseEntity<List<AlertResponseDto>> alerts(@RequestHeader("Authorization") String token) {
    Member member = memberService.findMemberByEmail(tokenService.getMemberEmail(token));

    return ResponseEntity.ok(alertService.getAlerts(member.getMemberId()));
  }

  /**
   * 알림 읽음 처리
   * @param alarmId
   */
  @PatchMapping("/{alertId}")
  public ResponseEntity<Long> check(
      @PathVariable("alertId") Long alarmId,
      @RequestHeader("Authorization") String token
  ) {
    Member member = memberService.findMemberByEmail(tokenService.getMemberEmail(token));
    Long alertCnt = alertService.check(member.getMemberId(), alarmId);
    return ResponseEntity.ok(alertCnt);
  }

  /**
   * 알림 삭제 처리
   * @param alarmId
   */
  @DeleteMapping("/{alertId}")
  public ResponseEntity<Long> delete(
      @PathVariable("alertId") Long alarmId,
      @RequestHeader("Authorization") String token
  ) {
    Member member = memberService.findMemberByEmail(tokenService.getMemberEmail(token));
    Long alertCnt = alertService.delete(member.getMemberId(), alarmId);
    return ResponseEntity.ok(alertCnt);
  }

}
