package com.valuewith.tweaver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**")
          .allowedOriginPatterns("https://tweaver.site")
          .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "PATCH", "OPTIONS")
          .allowedHeaders("")
          .allowCredentials(true);
    }
}
