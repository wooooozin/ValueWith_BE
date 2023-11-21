package com.valuewith.tweaver.auth.handler;

import static com.valuewith.tweaver.constants.ErrorCode.UNVALIDATED_REDIRECT_URI;

import com.valuewith.tweaver.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.valuewith.tweaver.commons.PrincipalDetails;
import com.valuewith.tweaver.commons.security.service.CookieService;
import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.config.AppPropertiesConfig;
import com.valuewith.tweaver.exception.CustomException;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final HttpCookieOAuth2AuthorizationRequestRepository cookieRequestRepository;
  private final AppPropertiesConfig appProperties;
  private final TokenService tokenService;
  private final MemberRepository memberRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    String targetUrl = determineTargetUrl(request, response, authentication);
    /*
      ServlerResponse.isCommitted()는 응답이 이미 클라이언트에 커밋되었는지 여부를 확인합니다.
      응답 내용을 쓰기 위해 서블릿 출력 스트림이 열렸음을 의미합니다.
      내용: https://stackoverflow.com/questions/39725888/what-does-httpservletresponse-is-committed-mean
     */
    if (response.isCommitted()) {
      System.out.println("이미 커밋된 response 입니다. redirect 불가능 주소: " + targetUrl);
      return;
    }

    // 인증정보 요청 내역(쿠키) 삭제
    clearAuthenticationAttributes(request, response);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  protected void clearAuthenticationAttributes(HttpServletRequest request,
      HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    cookieRequestRepository.removeAuthorizationRequestCookies(request, response);
  }

  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    Optional<String> redirectUri = CookieService.getCookie(request,
            HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
        .map(Cookie::getValue);

    if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
      throw new CustomException(UNVALIDATED_REDIRECT_URI);
    }

    String targetUri = redirectUri.orElse(getDefaultTargetUrl());
    String refreshToken = tokenService.createRefreshToken();

    return UriComponentsBuilder
        .fromUriString(targetUri).queryParam("refreshToken", refreshToken)
        .build().toUriString();
  }

  private boolean isAuthorizedRedirectUri(String redirectUri) {
    URI clientRedirectUri = URI.create(redirectUri);
    System.out.println("client redirect uri: " + redirectUri);
    return appProperties.getOAuth2().getAuthorizedRedirectUris()
        .stream()
        .anyMatch(authorizedRedirectUri -> {
          URI authorizedUri = URI.create(authorizedRedirectUri);

          // Host와 Port만 검증: client에서 다른 uri를 쓸 수 있기 때문이다.
          return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
              && authorizedUri.getPort() == clientRedirectUri.getPort();
        });
  }
}
