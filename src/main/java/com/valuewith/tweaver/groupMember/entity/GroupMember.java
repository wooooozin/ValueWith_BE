package com.valuewith.tweaver.groupMember.entity;

import com.valuewith.tweaver.auditing.BaseEntity;
import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.constants.MemberRole;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.message.entity.Message;
import com.valuewith.tweaver.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GROUP_MEMBER")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE GROUP_MEMBER SET IS_DELETED = 1 WHERE GROUP_MEMBER_ID = ?")
public class GroupMember extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long groupMemberId;

  @NotNull
  @Enumerated(EnumType.STRING)
  private MemberRole memberRole;

  @NotNull
  private Boolean isBanned;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ApprovedStatus approvedStatus;

  private LocalDateTime approvedDateTime;

  @OneToMany(mappedBy = "groupMember")
  @Builder.Default
  private List<Message> messages = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trip_group_id")
  private TripGroup tripGroup;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id")
  private ChatRoom chatRoom;
}
