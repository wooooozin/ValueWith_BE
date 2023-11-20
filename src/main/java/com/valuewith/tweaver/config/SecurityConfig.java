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
import java.util.Arrays;
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
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

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
    http
        .cors(cors -> cors.configurationSource(request -> {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOrigins(Arrays.asList(
              "https://tweaver.vercel.app",
              "http://localhost:5173",
              "http://127.0.0.1:5173"
              ));
          config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
          config.setAllowedHeaders(Arrays.asList("*"));
          config.setAllowCredentials(true);
          return config;
        }))
        .csrf().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeHttpRequests(authz -> authz
            .antMatchers(
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html",
                "/h2-console/**",
                "/",
                "/auth/**",
                "/login/**",
                "/chat/**",
                "/pub/**",
                "/sub/**",
                "/oauth/**",
                "/alert",
                "/alert/**",
                "/users/**",
                "/groups/**",
                "/images/**"
            )
            .permitAll()
            // 회원만 들어갈 수 있는 API는 현재 Security에서 거르지 못합니다.
            // UserDetails를 상속받는
            // commons.PrincipalDetails 엔티티에서 authorities를 사용하면 세부 설정이 가능합니다.
            .anyRequest().authenticated()
        )
        .formLogin().disable()
        .httpBasic().disable()
        .headers().frameOptions().disable()

        .and()
        .addFilterAfter(customJsonAuthenticationFilter(), LogoutFilter.class)
        .addFilterBefore(jwtAuthenticationFilter(), CustomJsonAuthenticationFilter.class)
        .logout(logout -> logout.logoutSuccessUrl("/"))

        .oauth2Login(oauth2 -> oauth2
            .authorizationEndpoint()
            .baseUri("/oauth2/authorization")
            .authorizationRequestRepository(oAuth2AuthorizationRequestRepository())
            .and()
            .userInfoEndpoint()
            .userService(oAuthUserCustomService)
        );

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
