package com.valuewith.tweaver.auth.info;

import java.util.Map;
import java.util.StringTokenizer;

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

  @Override
  public String getProfileUrl() {
    return getValue("kakao_account", "profile", "profile_image_url");
  }

  @Override
  public String getGender() {
    return getValue("kakao_account", "gender");
  }

  @Override
  public Integer getAge() {
    String age = getValue("kakao_account", "age_range");
    assert age != null;
    StringTokenizer st = new StringTokenizer(age, "~");
    age = st.nextToken();

    return Integer.parseInt(age);
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
