package com.valuewith.tweaver.auth.info;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

  public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getEmail() {
    return getValue("kakao_account", "email");
  }

  @Override
  public String getName() {
    return getValue("kakao_account", "profile", "nickname");
  }

  @Override
  public String getProviderId() {
    return attributes.get("id").toString();
  }

  public String getProfileUrl() {
    return getValue("kakao_account", "profile", "profile_image");
  }

  private String getValue(String... keys) {
    Map<String, Object> current = attributes;
    for (String key : keys) {
      Object value = current.get(key);
      if (value == null) {
        return null;
      }
      if (value instanceof Map) {
        current = (Map<String, Object>) value;
      } else {
        return (String) value;
      }
    }
    return current.toString();
  }
}
