package com.valuewith.tweaver.auth.controller;

import com.valuewith.tweaver.auth.dto.AuthDto;
import com.valuewith.tweaver.auth.dto.AuthDto.EmailInput;
import com.valuewith.tweaver.auth.dto.AuthDto.SignUpForm;
import com.valuewith.tweaver.auth.dto.AuthDto.VerificationForm;
import com.valuewith.tweaver.auth.service.AuthService;
import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth", produces = "application/json; charset=utf8")
public class AuthController {

  private final AuthService authService;
  private final TokenService tokenService;

  @PostMapping(value = "/signin")
  public ResponseEntity<String> signIn(@RequestBody AuthDto.SignInForm request) {
    Member member = authService.authenticate(request);
    return ResponseEntity.ok(tokenService.createAccessToken(member.getEmail()));
  }

  @PostMapping(value = "/signup")
  public void signUp(SignUpForm request, MultipartFile file) {
    authService.signUp(request, file);
  }

  @PostMapping("/verify")
  public void sendCode(EmailInput request) {
    authService.sendEmailVerification(request);
  }

  @PostMapping("/verify/check")
  public ResponseEntity<Boolean> checkCode(VerificationForm request) {
    return ResponseEntity.ok(authService.isVerified(request));  // true
  }
}
