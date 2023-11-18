package com.valuewith.tweaver.auth.service;

import com.valuewith.tweaver.auth.dto.AuthDto;
import com.valuewith.tweaver.auth.dto.AuthDto.SignInForm;
import com.valuewith.tweaver.auth.dto.AuthDto.TokensAndMemberId;
import com.valuewith.tweaver.auth.dto.LoginMemberIdDto;
import com.valuewith.tweaver.commons.redis.RedisUtilService;
import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.constants.ImageType;
import com.valuewith.tweaver.defaultImage.entity.DefaultImage;
import com.valuewith.tweaver.defaultImage.repository.DefaultImageRepository;
import com.valuewith.tweaver.defaultImage.service.ImageService;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;
  private final DefaultImageRepository defaultImageRepository;
  private final EmailService emailService;
  private final RedisUtilService redisUtilService;
  private final ImageService imageService;
  private final TokenService tokenService;


  public Member authenticate(SignInForm request) {
    /**
     * TODO: 커스텀 Exception 예정
     * 1. 이메일 확인
     * 2. 비밀번호 확인
     */

    // 1. 이메일 확인
    Member member = memberRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

    // 2. 비밀번호 확인
    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
      throw new RuntimeException("비밀번호가 다릅니다.");
    }

    // 3. 토큰 발행
    return member;
  }

  @Transactional
  public void signUp(AuthDto.SignUpForm form, MultipartFile file) {
    String profileUrl = "";
    if (file != null && !file.isEmpty()) {
      // 사진을 받아온 경우 이미지 등록
      profileUrl = imageService.uploadImageAndGetUrl(file, ImageType.MEMBER);
    } else {
      // 사진을 못받은 경우 기본 이미지 등록
      // TODO: 기본 프로필 이미지 업로드 되면 다시 확인 (23년 11월 16일 1차 작업중)
      DefaultImage defaultImg = defaultImageRepository.findByImageName("멤버");
      profileUrl = defaultImg.getDefaultImageUrl();
    }
    // 비밀번호 암호화
    form.setPassword(this.passwordEncoder.encode(form.getPassword()));

    memberRepository.save(form.setProfileUrl(profileUrl));
  }

  public void sendEmailVerification(AuthDto.EmailInput input) {
    String receiver = input.getEmail();
    if (isEmailExist(receiver)) {
      // TODO: 커스텀 Exception 적용
      throw new RuntimeException("이미 사용중인 이메일 입니다.");
    }
    emailService.sendCodeForValid(receiver);
  }

  public Boolean isVerified(AuthDto.VerificationForm form) {
    String emailCode = form.getCode();
    String savedCode = redisUtilService.getData(form.getEmail());
    if (savedCode.isEmpty()) {
      // TODO: 커스텀 Exception 적용
      throw new RuntimeException("만료된 코드 입니다.");
    }
    if (!emailCode.equals(savedCode)) {
      // TODO: 커스텀 Exception 적용
      throw new RuntimeException("인증코드가 다릅니다.");
    }
    return Boolean.TRUE;
  }

  public Boolean isEmailExist(String email) {
    return memberRepository.existsByEmail(email.toLowerCase(Locale.ROOT));
  }

  public TokensAndMemberId reissueTwoTokens(HttpServletResponse response, String refreshToken) {
    Optional<Member> member = memberRepository.findByRefreshToken(refreshToken);
    if (member.isEmpty()) {
      return null;
    }
    String newRefreshToken = reissueRefreshToken(member.get());
    String newAccessToken = tokenService.createAccessToken(member.get().getEmail());

    tokenService.sendAccessToken(response, newAccessToken);
    tokenService.sendAccessTokenAndRefreshToken(response, newAccessToken, newRefreshToken);

    LoginMemberIdDto loginMember = LoginMemberIdDto.from(member.get());
    return TokensAndMemberId.from(newAccessToken, newRefreshToken, loginMember);
  }

  private String reissueRefreshToken(Member member) {
    String newRefreshToken = tokenService.createRefreshToken();
    member.updateRefreshToken(newRefreshToken);
    memberRepository.saveAndFlush(member);
    return newRefreshToken;
  }
}
