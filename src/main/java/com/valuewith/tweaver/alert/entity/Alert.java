package com.valuewith.tweaver.alert.entity;

import com.valuewith.tweaver.auditing.BaseEntity;
import com.valuewith.tweaver.menber.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Table(name = "ALERT")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE ALERT SET IS_DELETED = 1 WHERE ALERT_ID = ?")
public class Alert extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long alertId;

  private Boolean isChecked;

  private String userToken;

  private Long groupId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;
}
