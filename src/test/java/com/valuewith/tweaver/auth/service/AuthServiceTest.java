package com.valuewith.tweaver.auth.service;

import com.valuewith.tweaver.defaultImage.service.ImageService;
import com.valuewith.tweaver.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
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

  @Autowired
  MockMvc mockMvc;
  @Autowired
  Environment env;

  /**
   * TODO: 회원가입 테스트, 로그인 테스트
   * 현재 UnsatisfiedDependencyException 해결중입니다.
    */

}