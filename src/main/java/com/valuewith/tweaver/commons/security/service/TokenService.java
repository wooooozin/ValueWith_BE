package com.valuewith.tweaver.commons.security.service;

import static com.valuewith.tweaver.constants.ErrorCode.*;

import com.valuewith.tweaver.constants.ErrorCode;
import com.valuewith.tweaver.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Getter
public class TokenService {

  private static final Long ACCESS_TOKEN_VALID_TIME = 1000L * 60L * 5L;  // 1시간 -> test용 5분
  private static final Long REFRESH_TOKEN_VALID_TIME = 1000L * 60L * 60L * 24L * 7L;  // 7일
  private static final String ACCESS_SUBJECT = "Access";
  private static final String REFRESH_SUBJECT = "Refresh";
  private static final String CLAIM_EMAIL = "email";
  private static final String BEARER = "Bearer ";

  private final String accessHeader = "Authorization";
  private final String refreshHeader = "RefreshToken";

  @Value("${jwt-secret-key}")
  private String secretKey;

  private final PrincipalService principalService;

  /**
   * Access 토큰을 생성합니다. 페이로드에 들어갈 기본적인 정보는 다음과 같습니다. 1. subject: Access 2. expiration: 1시간 3. claim:
   * email 변경사항 있을시에 claims에 put(클레임 이름, 값) 형식으로 추가해주세요. 클레임 이름은 상수로 추가해주세요. (파싱에서 사용)
   */
  public String createAccessToken(String email) {
    Claims claims = Jwts.claims().setSubject(ACCESS_SUBJECT);
    claims.put(CLAIM_EMAIL, email);

    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS512, this.secretKey)  // HMAC 기반 인증
        .compact();
  }

  /**
   * Refresh 토큰을 생성합니다. 페이로드에 들어갈 기본적인 정보는 다음과 같습니다. 1. subject: Refresh 2. expiration: 7일(1주)
   * Refresh 토큰에는 별도의 클레임이 들어가지 않습니다.
   */
  public String createRefreshToken() {
    Claims claims = Jwts.claims().setSubject(REFRESH_SUBJECT);
    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS512, this.secretKey)  // HMAC 기반 인증
        .compact();
  }

  /**
   * 토큰의 이메일 정보를 가져옵니다.
   */
  public String getMemberEmail(String token) {
    try {
      if (StringUtils.hasText(token) && token.startsWith(BEARER)) {
        token = token.replace(BEARER, "");
      }
      return this.parseClaims(token).get(CLAIM_EMAIL).toString();
    } catch (Exception e) {
      System.out.println("들어온 token: " + token);
      throw new CustomException(INVALID_JWT);
    }
  }

  /**
   * 토큰의 유효시간을 검증합니다.
   */
  public Boolean isValidToken(String token) {
    try {
      if (!StringUtils.hasText(token)) {
        return Boolean.FALSE;
      }
      Claims claims = parseClaims(token);
      return claims.getExpiration().after(new Date());
    } catch (Exception e) {
      System.out.println("들어온 token: " + token);
      throw new CustomException(INVALID_JWT);
    }
  }

  /**
   * token을 해독하여 claim값들을 반환합니다.
   */
  private Claims parseClaims(String token) {
    try {
      return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  /**
   * 헤더에 Authorization: "Bearer {token}" 형식으로 오게 됩니다.
   * 이때 "Bearer "를 지우고 "{token}" 값으로 파싱합니다.
   */
  public String parseAccessToken(HttpServletRequest request) {
    String fullToken = request.getHeader(accessHeader);
    if (StringUtils.hasText(fullToken) && fullToken.startsWith(BEARER)) {
      return fullToken.replace(BEARER, "");
    }
    return "";  // TODO: 204 얘정 -eod940.23.11.20
  }

  public Optional<String> parseRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(refreshHeader))
        .filter(refresh -> refresh.startsWith(BEARER))
        .map(refresh -> refresh.replace(BEARER, ""));
  }

  // 재발급된 Access 토큰을 헤더에 넣어서 보냅니다.
  public void sendAccessToken(HttpServletResponse response, String accessToken) {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setHeader(accessHeader, accessToken);
  }

  // Access 토큰, Refresh 토큰을 모두 헤더에 넣어서 보냅니다.
  public void sendAccessTokenAndRefreshToken(HttpServletResponse response,
      String accessToken, String refreshToken) {
    response.setStatus(HttpServletResponse.SC_OK);

    setAccessTokenToHeader(response, BEARER + accessToken);
    setRefreshTokenToHeader(response, BEARER + refreshToken);
    setRefreshTokenToCookie(response, refreshToken);
  }

  public void setAccessTokenToHeader(HttpServletResponse response, String accessToken) {
    response.setHeader(accessHeader, accessToken);
  }

  public void setRefreshTokenToHeader(HttpServletResponse response, String refreshToken) {
    response.setHeader(refreshHeader, refreshToken);
  }

  public void setRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
    Cookie cookie = new Cookie(refreshHeader, refreshToken);
    cookie.setMaxAge(Math.toIntExact(REFRESH_TOKEN_VALID_TIME));  // 쿠키 만료
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    response.addCookie(cookie);
  }
}
