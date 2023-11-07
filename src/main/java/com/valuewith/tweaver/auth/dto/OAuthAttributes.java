package com.valuewith.tweaver.auth.dto;

import com.valuewith.tweaver.auth.info.KakaoOAuth2UserInfo;
import com.valuewith.tweaver.auth.info.OAuth2UserInfo;
import com.valuewith.tweaver.constants.Provider;
import com.valuewith.tweaver.member.entity.Member;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

  private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
  private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

  @Builder
  public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
    this.nameAttributeKey = nameAttributeKey;
    this.oauth2UserInfo = oauth2UserInfo;
  }

  public static OAuthAttributes of(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuthAttributes.builder()
        .nameAttributeKey(userNameAttributeName)
        .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
        .build();
  }

  public Member toEntity(Provider provider, OAuth2UserInfo oauth2UserInfo) {
    return Member.builder()
        .email(oauth2UserInfo.getEmail())
        .providerId(oauth2UserInfo.getProviderId())
        .provider(provider)
        .build();
  }
}
