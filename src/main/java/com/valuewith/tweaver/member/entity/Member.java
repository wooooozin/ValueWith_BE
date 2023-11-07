package com.valuewith.tweaver.member.entity;

import com.valuewith.tweaver.auditing.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "MEMBER")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE MEMBER SET IS_DELETED = 1 WHERE MEMBER_ID = ?")
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  @NotNull
  @Column(unique = true)
  private String email;

  @NotNull
  private String password;

  @NotNull
  private String nickName;

  @NotNull
  private Integer age;

  @NotNull
  private String gender;

  @NotNull
  private String profileUrl;

  private String refreshToken;  // refreshToken

  // OAuth2 사용
  private String provider;  // OAuth 인증 제공자 (카카오, 네이버, ...)
  private String providerId;  // provider의 pk

  /**
   * 인증 관련 모듈은 PrincipalDetails에 있습니다.
   */
}
