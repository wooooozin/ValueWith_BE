package com.valuewith.tweaver.groupMember.entity;

import com.valuewith.tweaver.auditing.BaseEntity;
import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.member.entity.Member;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "GROUP_MEMBER")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE group_member SET IS_DELETED = 1 WHERE GROUP_MEMBER_ID = ?")
@Where(clause = "IS_DELETED = 0")
public class GroupMember extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long groupMemberId;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ApprovedStatus approvedStatus;

  private LocalDateTime approvedDateTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trip_group_id")
  private TripGroup tripGroup;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id")
  private ChatRoom chatRoom;

  public static GroupMember from(TripGroup tripGroup, Member member) {
    return GroupMember.builder()
        .approvedStatus(ApprovedStatus.PENDING)
        .approvedDateTime(LocalDateTime.now())
        .member(member)
        .tripGroup(tripGroup)
        .build();
  }

  public void rejectApplication() {
    this.approvedStatus = ApprovedStatus.REJECTED;
  }
  public void confirmApplication(ChatRoom chatRoom) {
    this.approvedStatus = ApprovedStatus.APPROVED;
    this.chatRoom = chatRoom;
  }

  public static GroupMember enterChatRoom(ChatRoom chatRoom, GroupMember groupMember) {
    return GroupMember.builder()
        .approvedStatus(groupMember.approvedStatus)
        .approvedDateTime(groupMember.approvedDateTime)
        .member(groupMember.member)
        .tripGroup(groupMember.tripGroup)
        .chatRoom(chatRoom)
        .build();
  }
  public void leaveApplication(ApprovedStatus status) {
    this.approvedStatus = status;
    this.isDeleted = true;
  }

  public void cancelApplication() {
    this.approvedStatus = ApprovedStatus.CANCEL;
    this.isDeleted = true;
  }
}
