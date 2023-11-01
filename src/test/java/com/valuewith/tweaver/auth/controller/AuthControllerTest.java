package com.valuewith.tweaver.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valuewith.tweaver.auth.service.AuthService;
import com.valuewith.tweaver.defaultImage.service.ImageService;
import com.valuewith.tweaver.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
class AuthControllerTest {

  @MockBean
  AuthService authService;
  @MockBean
  ImageService imageService;
  @MockBean
  GlobalExceptionHandler globalExceptionHandler;

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  MockMvc mockMvc;

  @Test
  @WithMockUser
  @DisplayName("회원가입 성공-이미지 있음")
  void success_signup_test_with_image() throws Exception {
    /**
     * 회원가입 성공여부는 SignUpForm 필드가 모두 작성될 경우입니다.
     * 이미지가 있는 경우
     */
    //given
    //when
    //then


  }
}