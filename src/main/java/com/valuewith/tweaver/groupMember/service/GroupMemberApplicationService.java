package com.valuewith.tweaver.groupMember.service;

import com.valuewith.tweaver.alert.dto.AlertRequestDto;
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
import org.springframework.context.ApplicationEventPublisher;
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

  private final ApplicationEventPublisher eventPublisher;

  public void createApplication(Long tripGroupId, String memberEmail) {
    TripGroup tripGroup = tripGroupRepository.findByTripGroupId(tripGroupId)
        .orElseThrow(() -> new RuntimeException("신청하고자 하는 그룹이 존재하지 않습니다."));
    Member member = memberRepository.findByEmail(memberEmail)
        .orElseThrow(() -> new RuntimeException("멤버 정보가 존재하지 않습니다."));
    Boolean exists = groupMemberRepository.existsByMember_MemberIdAndTripGroup_TripGroupId(
        member.getMemberId(), tripGroupId);
    if(exists) {
      throw new RuntimeException("이미 신청한 그룹 입니다. 같은 그룹에 중복 신청할 수 없습니다.");
    } else {
      groupMemberRepository.save(GroupMember.from(tripGroup, member));
    }

    // 신청이 왔을 때 알람 보내기
    eventPublisher.publishEvent(AlertRequestDto.builder()
            .groupId(tripGroupId)
            .member(tripGroup.getMember())
            .content(AlertContent.NEW_APPLICATION)
            .build());
  }

  public void deleteApplication(Long tripGroupId, String memberEmail) {
    Member member = memberRepository.findByEmail(memberEmail)
        .orElseThrow(() -> new RuntimeException("멤버가 존재하지 않습니다."));
    // 대기중인 신청 검색
    GroupMember foundGroupMember
        = groupMemberRepository.findPendingMembersByTripGroupIdAndMemberId(tripGroupId, member.getMemberId())
        .orElseThrow(() -> new RuntimeException("대기중인 신청이 존재하지 않습니다."));
    foundGroupMember.cancelApplication();
  }

  public void rejectApplication(Long groupMemberId) {
    GroupMember foundGroupMember
        = groupMemberRepository.findById(groupMemberId)
        .orElseThrow(() -> new RuntimeException("신청이 존재하지 않습니다."));
    foundGroupMember.rejectApplication();

    // 신청이 거절 되었을 때 알람 보내기
    eventPublisher.publishEvent(AlertRequestDto.builder()
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
    eventPublisher.publishEvent(AlertRequestDto.builder()
        .groupId(foundGroupMember.getTripGroup().getTripGroupId())
        .member(foundGroupMember.getMember())
        .content(AlertContent.APPLICATION_APPLY)
        .build());

    // 승인된 그룹에 다른 멤버들에게 추가 그룹원이 있다는 알람 보내기
    List<GroupMember> groupMembers
        = groupMemberRepository.findApprovedMembersByTripGroupIdAndNotInMemberId(
        foundGroupMember.getTripGroup().getTripGroupId(),
        foundGroupMember.getMember().getMemberId());
    groupMembers.stream().forEach(groupMember -> {
      eventPublisher.publishEvent(AlertRequestDto.builder()
          .groupId(groupMember.getTripGroup().getTripGroupId())
          .member(groupMember.getMember())
          .content(AlertContent.ADD_MEMBER)
          .build());
        }
    );
  }

}
