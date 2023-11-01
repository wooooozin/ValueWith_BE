package com.valuewith.tweaver.chat.entity;

import com.valuewith.tweaver.auditing.BaseEntity;
import com.valuewith.tweaver.group.entity.TripGroup;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "CHAT_ROOM")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE CHAT_ROOM SET IS_DELETED = 1 WHERE CHAT_ROOM_ID = ?")
public class ChatRoom extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long chatRoomId;

  @NotNull
  private String title;

  @OneToOne
  @JoinColumn(name = "trip_group_id")
  private TripGroup tripGroup;

  public void updateChatRoom(TripGroup tripGroup) {
    this.title = tripGroup.getName();
  }
}