package com.valuewith.tweaver.auth.service;

import static com.valuewith.tweaver.constants.ErrorCode.*;

import com.valuewith.tweaver.auth.dto.AuthDto;
import com.valuewith.tweaver.auth.dto.AuthDto.SignInForm;
import com.valuewith.tweaver.auth.dto.AuthDto.TokensAndMemberId;
import com.valuewith.tweaver.auth.dto.LoginMemberIdDto;
import com.valuewith.tweaver.commons.redis.RedisUtilService;
import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.constants.ErrorCode;
import com.valuewith.tweaver.constants.ImageType;
import com.valuewith.tweaver.defaultImage.entity.DefaultImage;
import com.valuewith.tweaver.defaultImage.repository.DefaultImageRepository;
import com.valuewith.tweaver.defaultImage.service.ImageService;
import com.valuewith.tweaver.exception.CustomException;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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


  @Transactional
  public void signUp(AuthDto.SignUpForm form, MultipartFile file) {
    String profileUrl = "";
    if (file != null && !file.isEmpty()) {
      // 사진을 받아온 경우 이미지 등록
      profileUrl = imageService.uploadImageAndGetUrl(file, ImageType.MEMBER);
    } else {
      // 사진을 못받은 경우 기본 이미지 등록
      // TODO: 기본 프로필 이미지 업로드 되면 다시 확인 - eod940(23.11.16)
      // TODO: 기본 프로필 이미지 업로드시 Custom Exception 제거 - eod940(23.11.20)
      try {
        DefaultImage defaultImg = defaultImageRepository.findByImageName("멤버");
        profileUrl = defaultImg.getDefaultImageUrl();
      } catch (Exception e) {
        throw new CustomException(FAILURE_GETTING_PROFILE_IMG);
      }
    }
    // 비밀번호 암호화
    form.setPassword(this.passwordEncoder.encode(form.getPassword()));

    memberRepository.save(form.setProfileUrl(profileUrl));
  }

  public void sendEmailVerification(AuthDto.EmailInput input) {
    String receiver = input.getEmail();
    if (isEmailExist(receiver)) {
      throw new CustomException(DUPLICATE_EMAIL);
    }
    emailService.sendCodeForValid(receiver);
  }

  public Boolean isVerified(AuthDto.VerificationForm form) {
    String emailCode = form.getCode();
    String savedCode = redisUtilService.getData(form.getEmail());
    if (savedCode.isEmpty()) {
      throw new CustomException(INVALID_CODE);
    }
    if (!emailCode.equals(savedCode)) {
      throw new CustomException(INCORRECT_CODE);
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
