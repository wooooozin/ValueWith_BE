package com.valuewith.tweaver.member.dto;

import com.valuewith.tweaver.member.entity.Member;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class SessionMemberDto implements Serializable {

  private String email;
  private String nickname;
  private String profileImage;

  public SessionMemberDto from(Member member) {
    this.email = member.getEmail();
    this.nickname = member.getNickName();
    this. profileImage = member.getProfileUrl();

    return this;
  }
}
