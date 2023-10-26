package com.valuewith.tweaver.group.controller;

import com.valuewith.tweaver.group.dto.GroupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("groups/*")
public class GroupController {
  @PostMapping
  public ResponseEntity<?> writeGroup(GroupDto groupDto) {
    return ResponseEntity.ok("ok");
  }
}
