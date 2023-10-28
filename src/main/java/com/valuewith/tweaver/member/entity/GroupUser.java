package com.valuewith.tweaver.member.entity;

import com.valuewith.tweaver.auditing.BaseEntity;
import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.constants.UserRole;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.message.entity.Message;
import com.valuewith.tweaver.user.entity.User;
import lombok.AccessLevel;
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
@Table(name = "GROUP_USER")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE GROUP_USER SET IS_DELETED = 1 WHERE MEMBER_ID = ?")
public class GroupUser extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  @NotNull
  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  @NotNull
  private Boolean isBanned;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ApprovedStatus approvedStatus;

  @NotNull
  private LocalDateTime approvedDateTime;

  @OneToMany(mappedBy = "member")
  private List<Message> messages = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private TripGroup tripGroup;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id")
  private ChatRoom chatRoom;
}
