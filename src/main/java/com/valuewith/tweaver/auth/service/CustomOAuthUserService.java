package com.valuewith.tweaver.auth.service;

import com.valuewith.tweaver.auth.dto.OAuthAttributes;
import com.valuewith.tweaver.constants.Provider;
import com.valuewith.tweaver.member.dto.MemberDto;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final MemberRepository memberRepository;

  /**
   * oAuth2User: userRequest에서 OAuth2User를 가져옵니다.
   *   - OAuth2User는 OAuth 서비스(ex. 카카오 관련 요소들)를 가지고 있습니다.
   * registrationID: OAuth 서비스 이름을 가져옵니다. (ex. {registrationId: "kakao"})
   *   - 현재는 kakao만 사용해 불필요하지만, 추후 다른 소셜로그인 기능 추가시 필요합니다. (추가시 주석 제거)
   * userNameAttributedName: OAuth 로그인시 키 값이 됩니다. (제공 사이트마다 다르므로 따로 변수선언을 해줍니다.)
   * attributes: OAuth 서비스의 유저 정보들을 가져옵니다.
   */
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration()
        .getRegistrationId();
    String userAttributedName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();
    Map<String, Object> attributes = oAuth2User.getAttributes();

    System.out.println("getClientRegistration: " + userRequest.getClientRegistration());
    System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());
    System.out.println("getAttributes: "+ delegate.loadUser(userRequest).getAttributes());

    Provider provider = getProvider(registrationId);

    // registrationId에 따라 유저 정보를 통해 공통된 MemberProfile 객체로 만들어 줌
    // OAuth2 로그인을 통해 가져온 OAuth2User의 attribute를 담아주는 of 메소드.
    OAuthAttributes extractAttr = OAuthAttributes.of(userAttributedName, attributes);

    Member member = getMember(extractAttr, provider);

//    return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
//        attributes.getAttributes(),
//        attributes.getNameAttributeKey());
    return null;
  }

  private Provider getProvider(String registrationId) {
    // 다른 소셜로그인이 있다면 추가해야합니다.
    return Provider.KAKAO;
  }

  /**
   * provider와 providerId로 유저를 찾습니다.
   * 유저가 없을 경우 회원가입 후 Member를 리턴합니다.
   */
  private Member getMember(OAuthAttributes extractAttribute, Provider provider) {

    return memberRepository.findByProviderAndProviderId(
            provider, extractAttribute.getOauth2UserInfo().getProviderId())
        .orElseGet(() -> saveOrUpdate(extractAttribute, provider));
  }

  private Member saveOrUpdate(OAuthAttributes extractAttribute, Provider provider) {
    Member member = extractAttribute.toEntity(provider, extractAttribute.getOauth2UserInfo());
    return memberRepository.save(member);
  }
}
