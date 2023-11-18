package com.valuewith.tweaver.groupMember.controller;

import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.groupMember.service.GroupMemberApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/groups/application/*")
public class GroupMemberApplicationController {

  private final TokenService tokenService;
  private final GroupMemberApplicationService groupMemberApplicationService;

  @PostMapping("{tripGroupId}")
  public ResponseEntity<String> createApplication(
      @PathVariable("tripGroupId") Long tripGroupId,
      @RequestHeader("Authorization") String token) {
    String memberEmail = tokenService.getMemberEmail(token);
    groupMemberApplicationService.createApplication(tripGroupId, memberEmail);

    return ResponseEntity.ok("ok");
  }

  @DeleteMapping("{tripGroupId}")
  public ResponseEntity<String> deleteApplication(
      @PathVariable("tripGroupId") Long tripGroupId,
      @RequestHeader("Authorization") String token) {
    String memberEmail = tokenService.getMemberEmail(token);
    groupMemberApplicationService.deleteApplication(tripGroupId, memberEmail);

    return ResponseEntity.ok("ok");
  }

  @PatchMapping("reject/{groupMemberId}")
  public ResponseEntity<String> rejectApplication(
      @PathVariable("groupMemberId") Long groupMemberId) {
    groupMemberApplicationService.rejectApplication(groupMemberId);

    return ResponseEntity.ok("ok");
  }

  @PatchMapping("confirm/{groupMemberId}")
  public ResponseEntity<String> confirmApplication(
      @PathVariable("groupMemberId") Long groupMemberId) {
    groupMemberApplicationService.confirmApplication(groupMemberId);

    return ResponseEntity.ok("ok");
  }
}
