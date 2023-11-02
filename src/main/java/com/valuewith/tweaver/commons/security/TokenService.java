package com.valuewith.tweaver.commons.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenService {

  // 토큰 유효시간은 1시간입니다.
  private static final Long TOKEN_VALID_TIME = 1000L * 60L * 60L;

  @Value("${jwt-secret-key}")
  private String secretKey;

  /**
   * 토큰을 생성합니다. email값을 포함한
   */
  public String generateToken(String email) {
    Claims claims = Jwts.claims().setSubject(email);
    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + TOKEN_VALID_TIME);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS512, this.secretKey)
        .compact();
  }

  public String getMemberEmail(String token) {
    return this.parseClaims(token).getSubject();
  }

  public Boolean isValidToken(String token) {
    if (!StringUtils.hasText(token)) {
      return Boolean.FALSE;
    }
    Claims claims = parseClaims(token);
    return claims.getExpiration().after(new Date());
  }

  private Claims parseClaims(String token) {
    try {
      return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
