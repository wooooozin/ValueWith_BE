package com.valuewith.tweaver.config;

import com.valuewith.tweaver.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.valuewith.tweaver.auth.service.OAuthUserCustomService;
import com.valuewith.tweaver.commons.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter authenticationFilter;
  private final OAuthUserCustomService oAuthUserCustomService;

  /**
   * swagger, h2-console 접근을 위한 설정입니다.
   * 인증이 필요한 URI 목록 중 아래에 있는 [허용 URL]의 모든 접근을 허용합니다.
   * 사실상 모든 요청("/**")에 접근을 허용했습니다.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable().sessionManagement().
        sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeHttpRequests().antMatchers(
            // 허용 URL
            "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources",
            "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui/**",
            "/webjars/**", "/swagger-ui.html",  "/**", "/h2-console")
        .permitAll().anyRequest().authenticated()
        .and()
        .headers().frameOptions().disable()

        .and()
        .addFilterBefore(this.authenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout.logoutSuccessUrl("/"))

        .oauth2Login()
        .authorizationEndpoint().baseUri("/oauth2/authorization")

        .and()
        .userInfoEndpoint()
        .userService(oAuthUserCustomService);

    return http.build();
  }

//  public HttpCookieOAuth2AuthorizationRequestRepository a() {
//
//  }
}
