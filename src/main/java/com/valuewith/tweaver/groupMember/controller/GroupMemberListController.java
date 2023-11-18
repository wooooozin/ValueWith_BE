package com.valuewith.tweaver.groupMember.controller;

import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.groupMember.dto.GroupMemberListDto;
import com.valuewith.tweaver.groupMember.service.GroupMemberListService;
import com.valuewith.tweaver.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PatchMapping("{tripGroupId}/member/{groupMemberId}/banned")
    public ResponseEntity<String> bannedGroupMemberFromTripGroup(
        @RequestHeader("Authorization") String token,
        @PathVariable Long tripGroupId,
        @PathVariable Long groupMemberId
    ) {
        String memberEmail = tokenService.getMemberEmail(token);
        Member member = groupMemberListService.bannedGroupMemberFromTripGroup(
            memberEmail, tripGroupId, groupMemberId
        );
        return ResponseEntity.ok( member.getNickName() + " 님을 추방 했습니다.");
    }
}
