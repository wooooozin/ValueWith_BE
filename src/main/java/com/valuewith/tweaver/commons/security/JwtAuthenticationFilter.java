package com.valuewith.tweaver.commons.security;

import com.valuewith.tweaver.commons.PrincipalDetails;
import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
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
  private final MemberRepository memberRepository;

  private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = resolveTokenFromRequest(request);

    // 토큰이 올바르다면 인증 정보를 Context에 저장하는 과정이 추가
    if (request.getRequestURI().equals("/auth/signin")
        && StringUtils.hasText(token)
        && tokenService.isValidToken(token)) {
      filterChain.doFilter(request, response);
      return;
    }

    // 리프레시 토큰이 유효한지 검증합니다.
    String refreshToken = tokenService
        .parseRefreshToken(request)
        .filter(tokenService::isValidToken)
        .orElse(null);

    /*
      헤더에 리프레시 토큰이 존재 -> 사용자 AccessToken 만료
      받은 리프레시 토큰을 DB와 비교 후 재발급 진행
     */
    if (refreshToken != null) {
      reissueAccessTokenAfterRefreshToken(response, refreshToken);
      return;
    }

    /*
      리프레시 토큰이 없거나 유효하지 않다면 사용자 AccessToken 검사가 필요하다.
      AccessToken이 없거나 유효하지 않다면 -> 403
      AccessToken이 있고 유효하다면 -> 200
     */
    authenticateAccessToken(request, response, filterChain);
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

  public void reissueAccessTokenAfterRefreshToken(HttpServletResponse response,
      String refreshToken) {
    memberRepository.findByRefreshToken(refreshToken).ifPresent(member ->
        {
          String newRefreshToken = reissueRefreshToken(member);
          tokenService.sendAccessTokenAndRefreshToken(
              response,
              tokenService.createAccessToken(member.getEmail()),
              newRefreshToken);
        }
    );
  }

  public void authenticateAccessToken(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token = tokenService.parseAccessToken(request);
    if (token != null && tokenService.isValidToken(token)) {
      String email = tokenService.getMemberEmail(token);
      memberRepository.findByEmail(email).ifPresent(this::saveAuthentication);
    }
    filterChain.doFilter(request, response);
  }

  private String reissueRefreshToken(Member member) {
    String newRefreshToken = tokenService.createRefreshToken();
    member.updateRefreshToken(newRefreshToken);
    memberRepository.saveAndFlush(member);
    return newRefreshToken;
  }

  public void saveAuthentication(Member member) {
    PrincipalDetails principalDetails = new PrincipalDetails(member);
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        principalDetails, "", authoritiesMapper.mapAuthorities(principalDetails.getAuthorities())
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
