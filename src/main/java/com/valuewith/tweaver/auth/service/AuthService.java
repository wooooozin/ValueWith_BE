package com.valuewith.tweaver.auth.service;

import com.valuewith.tweaver.auth.dto.AuthDto;
import com.valuewith.tweaver.commons.redis.RedisUtilService;
import com.valuewith.tweaver.constants.ImageType;
import com.valuewith.tweaver.defaultImage.service.ImageService;
import com.valuewith.tweaver.user.entity.Member;
import com.valuewith.tweaver.user.repository.UserRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final EmailService emailService;
  private final RedisUtilService redisUtilService;
  private final ImageService imageService;

  public Member signUp(AuthDto.SignUpForm form, MultipartFile file) {
    if (file.isEmpty()) {
      // TODO: 사용자로 부터 사진 등록을 받지 못한 경우 기본 이미지 등록
      form.setProfileUrl("https://");
    }
    String profileUrl = imageService.uploadImageAndGetUrl(file, ImageType.MEMBER);
    form.setProfileUrl(profileUrl);

    // 비밀번호 암호화
    form.setPassword(this.passwordEncoder.encode(form.getPassword()));

    return userRepository.save(form.toEntity());
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
    return userRepository.existsByEmail(email.toLowerCase(Locale.ROOT));
  }
}
