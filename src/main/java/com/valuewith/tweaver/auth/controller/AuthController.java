package com.valuewith.tweaver.auth.controller;

import com.valuewith.tweaver.auth.dto.AuthDto.*;
import com.valuewith.tweaver.auth.service.AuthService;
import com.valuewith.tweaver.user.entity.Member;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth", produces = "application/text; charset=utf8")
public class AuthController {

  private final AuthService authService;

  // TODO: 회원가입(등록), 이메일 인증

  @PostMapping(value = "/signup")
  public ResponseEntity<String> signUp(SignUpForm request, MultipartFile file) {
    return ResponseEntity.ok(authService.signUp(request, file));
  }

  @PostMapping("/verify")
  public void sendCode(@RequestBody EmailInput request) {
    authService.sendEmailVerification(request);
  }

  @PostMapping("/verify/check")
  public ResponseEntity<Boolean> checkCode(@RequestBody VerificationForm request) {
    return ResponseEntity.ok(authService.isVerified(request));  // true
  }
}
