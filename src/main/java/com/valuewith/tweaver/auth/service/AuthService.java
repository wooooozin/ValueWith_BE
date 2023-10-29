package com.valuewith.tweaver.auth.service;

import com.valuewith.tweaver.auth.client.mailgun.SendEmailForm;
import com.valuewith.tweaver.auth.dto.AuthDto;
import com.valuewith.tweaver.user.entity.Member;
import com.valuewith.tweaver.user.repository.UserRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final EmailService emailService;

  public boolean isEmailExist(String email) {
    return userRepository.existsByEmail(email.toLowerCase(Locale.ROOT));
  }

  public void emailVerification(AuthDto.SignUpForm form) {
     // TODO: 커스텀 Exception 적용
    if (isEmailExist(form.getEmail())) {
      throw new RuntimeException("이미 사용중인 이메일 입니다.");
    }
  }
  public Member signUp(AuthDto.SignUpForm form) {

    // 비밀번호 암호화
    form.setPassword(this.passwordEncoder.encode(form.getPassword()));

    return userRepository.save(form.toEntity());
  }

}
