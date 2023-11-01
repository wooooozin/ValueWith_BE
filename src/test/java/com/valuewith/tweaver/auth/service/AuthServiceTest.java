package com.valuewith.tweaver.auth.service;

import com.valuewith.tweaver.defaultImage.service.ImageService;
import com.valuewith.tweaver.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceTest {

  static {
    System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
  }

  @Autowired
  MemberRepository memberRepository;
  @Autowired
  AuthService authService;
  @Autowired
  ImageService imageService;

  @Test
  @DisplayName("회원가입 성공 테스트")
  void signup_test() throws Exception {
    // given
    // when
    // then
  }

}