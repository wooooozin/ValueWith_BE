package com.valuewith.tweaver.auth.info;

import java.util.Map;

public abstract class OAuth2UserInfo {
  Map<String, Object> attributes;

  public OAuth2UserInfo(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  public abstract String getEmail();

  public abstract String getName();

  public abstract String getProviderId();

  public abstract String getProfileUrl();

  public abstract String getGender();

  public abstract Integer getAge();
}
