package com.valuewith.tweaver.commons.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  private final TokenService tokenService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = resolveTokenFromRequest(request);

    // 토큰이 올바르다면 인증 정보를 Context에 저장하는 과정이 추가
//    if (StringUtils.hasText(token) && tokenService.isValidToken(token)) {
//      Authentication auth = tokenService.getAuthentication(token);
//      SecurityContextHolder.getContext().setAuthentication(auth);
//    }

    filterChain.doFilter(request, response);
  }

  /**
   * 클라이언트에서는 로그인 이후 request headers에 Authorization: Bearer {token} 값이 추가되어야 합니다.
   */
  private String resolveTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader(TOKEN_HEADER);

    // 토큰에서 "Bearer "를 없엔 뒤 토큰 값만 추출
    if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
      return token.substring(TOKEN_PREFIX.length());
    }

    return null;
  }
}
