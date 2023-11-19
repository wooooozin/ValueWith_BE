package com.valuewith.tweaver.member.entity;

import com.valuewith.tweaver.auditing.BaseEntity;
import com.valuewith.tweaver.constants.Provider;
import com.valuewith.tweaver.member.dto.MemberDto;
import com.valuewith.tweaver.message.entity.Message;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
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
@SQLDelete(sql = "UPDATE member SET IS_DELETED = 1 WHERE MEMBER_ID = ?")
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

  @OneToMany(mappedBy = "member")
  @Builder.Default
  private List<Message> messages = new ArrayList<>();

  // OAuth2 사용
  @Enumerated(EnumType.STRING)
  private Provider provider;  // OAuth 인증 제공자 (카카오, 네이버, ...)
  private String providerId;  // provider의 pk

  /*
    인증 관련 모듈은 PrincipalDetails에 있습니다.
   */
  public Member update(String nickName, String profileUrl) {
    this.nickName = nickName;
    this.profileUrl = profileUrl;

    return this;
  }

  public void updateRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public static Member from(MemberDto memberDto) {
    return Member.builder()
        .email(memberDto.getEmail())
        .password(memberDto.getPassword())
        .nickName(memberDto.getNickName())
        .age(memberDto.getAge())
        .gender(memberDto.getGender())
        .profileUrl(memberDto.getProfileUrl())
        .provider(memberDto.getProvider())
        .providerId(memberDto.getProviderId())
        .refreshToken(memberDto.getRefreshToken())
        .build();
  }
}
