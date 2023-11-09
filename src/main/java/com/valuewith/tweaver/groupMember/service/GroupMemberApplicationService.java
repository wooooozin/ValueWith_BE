package com.valuewith.tweaver.groupMember.service;

import com.valuewith.tweaver.alert.dto.AlertRequestDto;
import com.valuewith.tweaver.alert.service.AlertService;
import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.repository.ChatRoomRepository;
import com.valuewith.tweaver.constants.AlertContent;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import com.valuewith.tweaver.groupMember.repository.GroupMemberRepository;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GroupMemberApplicationService {

  private final GroupMemberRepository groupMemberRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final MemberRepository memberRepository;
  private final TripGroupRepository tripGroupRepository;
  private final AlertService alertService;

  public void createApplication(Long tripGroupId, String memberEmail) {
    TripGroup tripGroup = tripGroupRepository.findByTripGroupId(tripGroupId)
        .orElseThrow(() -> new RuntimeException("신청하고자 하는 그룹이 존재하지 않습니다."));
    Member member = memberRepository.findByEmail(memberEmail)
        .orElseThrow(() -> new RuntimeException("멤버 정보가 존재하지 않습니다."));
    groupMemberRepository.save(GroupMember.from(tripGroup, member));

    // 신청이 왔을 때 알람 보내기
    alertService.send(AlertRequestDto.builder()
        .userToken("aaa")
        .groupId(tripGroupId)
        .member(tripGroup.getMember())
        .content(AlertContent.NEW_APPLICATION)
        .build());
  }

  public void deleteApplication(Long groupMemberId, String memberEmail) {
    // 본인 신청만 삭제 할 수 있도록 신청자와 로그인자 비교(다른 사용자가 url로 접근하여 삭제 할 수 없게)
    GroupMember foundGroupMember
        = groupMemberRepository.findById(groupMemberId)
        .orElseThrow(() -> new RuntimeException("신청이 존재하지 않습니다."));
    if(memberEmail.equals(foundGroupMember.getMember().getEmail())) {
      groupMemberRepository.deleteById(groupMemberId);
    } else {
      throw new RuntimeException("잘못된 접근 입니다.");
    }
  }

  public void rejectApplication(Long groupMemberId) {
    GroupMember foundGroupMember
        = groupMemberRepository.findById(groupMemberId)
        .orElseThrow(() -> new RuntimeException("신청이 존재하지 않습니다."));
    foundGroupMember.rejectApplication();

    // 신청이 거절 되었을 때 알람 보내기
    alertService.send(AlertRequestDto.builder()
        .userToken("aaa")
        .groupId(foundGroupMember.getTripGroup().getTripGroupId())
        .member(foundGroupMember.getMember())
        .content(AlertContent.APPLICATION_REJECT)
        .build());
  }

  public void confirmApplication(Long groupMemberId) {
    GroupMember foundGroupMember
        = groupMemberRepository.findById(groupMemberId)
        .orElseThrow(() -> new RuntimeException("신청이 존재하지 않습니다."));
    ChatRoom chatRoom = chatRoomRepository.findByTripGroup(foundGroupMember.getTripGroup())
        .orElseThrow(() -> new RuntimeException("신청하고자 하는 그룹의 채팅방이 존재하지 않습니다."));
    foundGroupMember.confirmApplication(chatRoom);

    // 신청이 승인 되었을 때 알람 보내기
    alertService.send(AlertRequestDto.builder()
        .userToken("aaa")
        .groupId(foundGroupMember.getTripGroup().getTripGroupId())
        .member(foundGroupMember.getMember())
        .content(AlertContent.APPLICATION_APPLY)
        .build());

    // 승인된 그룹에 다른 멤버들에게 추가 그룹원이 있다는 알람 보내기
    List<GroupMember> groupMembers
        = groupMemberRepository.findApprovedMembersByTripGroupIdAndMemberId(
            foundGroupMember.getTripGroup().getTripGroupId(),
            foundGroupMember.getTripGroup().getMember().getMemberId(),
            foundGroupMember.getMember().getMemberId());
    groupMembers.stream().forEach(groupMember -> {
      alertService.send(AlertRequestDto.builder()
          .userToken("aaa")
          .groupId(groupMember.getTripGroup().getTripGroupId())
          .member(groupMember.getMember())
          .content(AlertContent.ADD_MEMBER)
          .build());
        }
    );
  }

}
