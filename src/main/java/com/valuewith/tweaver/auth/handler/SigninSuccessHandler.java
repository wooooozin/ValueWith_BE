package com.valuewith.tweaver.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valuewith.tweaver.auth.dto.AuthDto.TokensAndMemberId;
import com.valuewith.tweaver.auth.dto.LoginMemberIdDto;
import com.valuewith.tweaver.commons.PrincipalDetails;
import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@RequiredArgsConstructor
public class SigninSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final static String CONTENT_TYPE = "application/json";
  private final static String CHARACTER_ENCODING = "UTF-8";

  private final TokenService tokenService;
  private final MemberRepository memberRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    String email = getMemberEmail(authentication);

    Member member = memberRepository.findByEmail(email).orElseThrow(
        () -> new UsernameNotFoundException(email + "는(은) 없는 회원입니다."));
    String memberEmail = member.getEmail();

    String accessToken = tokenService.createAccessToken(memberEmail);
    String refreshToken = tokenService.createRefreshToken();

    // response header에 토큰 추가
    tokenService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);

    // 로그인한 회원의 RefreshToken 업데이트
    member.updateRefreshToken(refreshToken);
    memberRepository.saveAndFlush(member);

    // 멤버 식별자 보내주기
    LoginMemberIdDto memberData = LoginMemberIdDto.from(member);
    TokensAndMemberId memberIdData = TokensAndMemberId.from(accessToken, refreshToken, memberData);

    String memberJson = new ObjectMapper().writeValueAsString(memberIdData);

    response.setContentType(CONTENT_TYPE);
    response.setCharacterEncoding(CHARACTER_ENCODING);
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().write(memberJson);
  }

  private String getMemberEmail(Authentication authentication) {
    PrincipalDetails memberDetails = (PrincipalDetails) authentication.getPrincipal();
    return memberDetails.getUsername();
  }
}
