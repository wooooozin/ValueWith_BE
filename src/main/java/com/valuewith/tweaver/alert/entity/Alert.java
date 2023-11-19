package com.valuewith.tweaver.alert.entity;

import com.valuewith.tweaver.alert.dto.AlertRequestDto;
import com.valuewith.tweaver.auditing.BaseEntity;
import com.valuewith.tweaver.constants.AlertContent;
import com.valuewith.tweaver.constants.RedirectUrlType;
import com.valuewith.tweaver.member.entity.Member;
import javax.validation.constraints.NotNull;
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
@SQLDelete(sql = "UPDATE alert SET IS_DELETED = 1 WHERE ALERT_ID = ?")
public class Alert extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long alertId;

  @Enumerated(EnumType.STRING)
  @NotNull
  private AlertContent content;

  @NotNull
  private String redirectUrl;

  @NotNull
  private Boolean isChecked;

  @NotNull
  private Long groupId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  public void checkAlert() {
    this.isChecked = true;
  }

  public static Alert from(AlertRequestDto alertRequestDto) {
    AlertContent alertContent = alertRequestDto.getContent();
    String redirectUrl = "";

    /**
     * url 설정
     * 1. 그룹에 새로운 신청이 있는 경우에는 마이 라운지로 이동
     * 2. 그룹 내용이 변경된 겨우 그룹 상세페이지로
     */
    if (alertContent == AlertContent.NEW_APPLICATION) {
      redirectUrl = RedirectUrlType.MY_PAGE.getUrl();
    } else if(alertContent == AlertContent.UPDATE_GROUP_PLAN) {
      redirectUrl = RedirectUrlType.GROUP_DETAIL.getUrl() + alertRequestDto.getGroupId();
    }

    return Alert.builder()
        .member(alertRequestDto.getMember())
        .content(alertRequestDto.getContent())
        .redirectUrl(redirectUrl)
        .isChecked(false)
        .groupId(alertRequestDto.getGroupId())
        .build();
  }
}
