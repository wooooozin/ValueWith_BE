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
@SQLDelete(sql = "UPDATE ALERT SET IS_DELETED = 1 WHERE ALERT_ID = ?")
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
  private String userToken;

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
     * 1. 승인, 거부, 내가 신청한 그룹에 새로운 멤버 추가, 일정 수정 되었을 경우에는 그룹 상세페이지로
     * 2. 내가 만든 그룹의 새로운 신청자, 혹은 내 그룹이 삭제된 경우 나의 페이지로 이동
     */
    if (alertContent == AlertContent.APPLICATION_APPLY ||
        alertContent == AlertContent.APPLICATION_REJECT ||
        alertContent == AlertContent.ADD_MEMBER ||
        alertContent == AlertContent.UPDATE_GROUP_PLAN) {
      redirectUrl = RedirectUrlType.GROUP_DETAIL.getUrl() + alertRequestDto.getGroupId();
    } else {
      redirectUrl = RedirectUrlType.MY_PAGE.getUrl();
    }

    return Alert.builder()
        .member(alertRequestDto.getMember())
        .content(alertRequestDto.getContent())
        .redirectUrl(redirectUrl)
        .isChecked(false)
        .groupId(alertRequestDto.getGroupId())
        .userToken(alertRequestDto.getUserToken())
        .build();
  }
}
