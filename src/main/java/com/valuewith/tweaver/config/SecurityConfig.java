package com.valuewith.tweaver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valuewith.tweaver.auth.handler.SigninFailureHandler;
import com.valuewith.tweaver.auth.handler.SigninSuccessHandler;
import com.valuewith.tweaver.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.valuewith.tweaver.auth.service.CustomMemberDetailService;
import com.valuewith.tweaver.auth.service.OAuthUserCustomService;
import com.valuewith.tweaver.commons.security.CustomJsonAuthenticationFilter;
import com.valuewith.tweaver.commons.security.JwtAuthenticationFilter;
import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  private final OAuthUserCustomService oAuthUserCustomService;
  private final AuthConfig authConfig;
  private final CustomMemberDetailService customMemberDetailService;
  private final TokenService tokenService;
  private final MemberRepository memberRepository;

  /**
   * swagger, h2-console 접근을 위한 설정입니다. 인증이 필요한 URI 목록 중 아래에 있는 [허용 URL]의 모든 접근을 허용합니다. 사실상 모든
   * 요청("/**")에 접근을 허용했습니다.
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
            "/webjars/**", "/swagger-ui.html", "/**", "/h2-console")
        .permitAll().anyRequest().authenticated()
        .and()
        .headers().frameOptions().disable()

        .and()
        .addFilterAfter(customJsonAuthenticationFilter(), LogoutFilter.class)
        .addFilterBefore(jwtAuthenticationFilter(), CustomJsonAuthenticationFilter.class)
        .logout(logout -> logout.logoutSuccessUrl("/"))

        .oauth2Login()
        .authorizationEndpoint()
        .baseUri("/oauth2/authorization")
        .authorizationRequestRepository(oAuth2AuthorizationRequestRepository())
        .and()
        .userInfoEndpoint()
        .userService(oAuthUserCustomService);

    return http.build();
  }

  @Primary
  @Bean
  public HttpCookieOAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository() {
    return new HttpCookieOAuth2AuthorizationRequestRepository();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(tokenService, memberRepository);
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(authConfig.passwordEncoder());
    provider.setUserDetailsService(customMemberDetailService);
    return new ProviderManager(provider);
  }

  @Bean
  public SigninSuccessHandler signinSuccessHandler() {
    return new SigninSuccessHandler(tokenService, memberRepository);
  }

  @Bean
  public SigninFailureHandler signinFailureHandler() {
    return new SigninFailureHandler();
  }

  @Bean
  public CustomJsonAuthenticationFilter customJsonAuthenticationFilter() {
    CustomJsonAuthenticationFilter customJsonAuthenticationFilter = new CustomJsonAuthenticationFilter(
        new ObjectMapper());

    customJsonAuthenticationFilter.setAuthenticationManager(authenticationManager());
    customJsonAuthenticationFilter.setAuthenticationSuccessHandler(signinSuccessHandler());
    customJsonAuthenticationFilter.setAuthenticationFailureHandler(signinFailureHandler());
    return customJsonAuthenticationFilter;
  }
}
