package com.valuewith.tweaver.groupMember.controller;

import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import com.valuewith.tweaver.groupMember.dto.GroupMemberDto;
import com.valuewith.tweaver.groupMember.dto.GroupMemberListDto;
import com.valuewith.tweaver.groupMember.service.GroupMemberListService;
import com.valuewith.tweaver.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/groups/*")
public class GroupMemberListController {

    private final GroupMemberListService groupMemberListService;
    private final TokenService tokenService;

    @GetMapping("members/{tripGroupId}")
    public ResponseEntity<List<GroupMemberListDto>> getGroupMemberFilteredStatusList(
            @RequestHeader("Authorization") String token,
            @PathVariable Long tripGroupId,
            @RequestParam String status
    ) {
        String memberEmail = tokenService.getMemberEmail(token);
        List<GroupMemberListDto> groupMemberListDtoList
                = groupMemberListService.getFilteredGroupMembers(memberEmail, tripGroupId, status);
        return ResponseEntity.ok(groupMemberListDtoList);
    }


    @PatchMapping("{tripGroupId}/member/left")
    public ResponseEntity<String> leftMemberFromTripGroup(
        @RequestHeader("Authorization") String token,
        @PathVariable Long tripGroupId
    ) {
        String memberEmail = tokenService.getMemberEmail(token);
        groupMemberListService.leftMemberFromTripGroup(memberEmail, tripGroupId);
        return ResponseEntity.ok("여행그룹에서 나갔습니다.");
    }
}
