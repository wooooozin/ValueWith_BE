package com.valuewith.tweaver.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@ConfigurationProperties(prefix = "app")
public class AppPropertiesConfig {

  private final OAuth2 oAuth2 = new OAuth2();

  @Getter
  @Setter
  public static class OAuth2 {

    private List<String> authorizedRedirectUris = new ArrayList<>();
  }
}
