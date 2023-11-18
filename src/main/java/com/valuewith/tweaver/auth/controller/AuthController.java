package com.valuewith.tweaver.auth.controller;

import com.valuewith.tweaver.auth.dto.AuthDto.EmailInput;
import com.valuewith.tweaver.auth.dto.AuthDto.SignUpForm;
import com.valuewith.tweaver.auth.dto.AuthDto.TokensAndMemberId;
import com.valuewith.tweaver.auth.dto.AuthDto.VerificationForm;
import com.valuewith.tweaver.auth.service.AuthService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping(value = "/signup")
  public void signUp(@Valid SignUpForm request,
      @RequestPart(required = false) MultipartFile file) {
    authService.signUp(request, file);
  }

  @PostMapping("/verify")
  public void sendCode(@Valid @RequestBody EmailInput request) {
    authService.sendEmailVerification(request);
  }

  @PostMapping("/verify/check")
  public ResponseEntity<Boolean> checkCode(@RequestBody VerificationForm request) {
    return ResponseEntity.ok(authService.isVerified(request));  // true
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokensAndMemberId> reissueAccessToken(HttpServletResponse response,
      @RequestBody String refreshToken) {

    TokensAndMemberId memberData = authService.reissueTwoTokens(response, refreshToken);

    return ResponseEntity.ok(memberData);
  }
}
