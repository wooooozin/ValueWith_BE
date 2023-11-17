package com.valuewith.tweaver.commons.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valuewith.tweaver.auth.dto.AuthDto.SignInForm;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class CustomJsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final String DEFAULT_LOGIN_URI = "/auth/signin";
  private static final String HTTP_METHOD = "POST";
  private static final String CONTENT_TYPE = "application/json";
  private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher(DEFAULT_LOGIN_URI, HTTP_METHOD);

  public CustomJsonAuthenticationFilter(ObjectMapper objectMapper) {
    super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);  // 로그인 요청 처리
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    if (request.getContentType() == null
        || !request.getContentType().equals(CONTENT_TYPE)) {
      throw new AuthenticationServiceException(
          "Content-Type이 지원되지 않는 형식입니다: " + request.getContentType());
    }

    ObjectMapper objectMapper = new ObjectMapper();
    SignInForm signInForm = objectMapper.readValue(request.getInputStream(), SignInForm.class);
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        signInForm.getEmail(),
        signInForm.getPassword()
    );
    return getAuthenticationManager().authenticate(authenticationToken);
  }
}
