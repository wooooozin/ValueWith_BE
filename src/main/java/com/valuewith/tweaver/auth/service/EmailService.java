package com.valuewith.tweaver.auth.service;

import com.valuewith.tweaver.auth.client.MailgunClient;
import com.valuewith.tweaver.auth.client.mailgun.SendEmailForm;
import com.valuewith.tweaver.commons.redis.RedisUtilService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final MailgunClient mailgunClient;
  private final RedisUtilService redisUtilService;

  public String sendMail(SendEmailForm sendForm) {
    return mailgunClient.sendEmail(sendForm).getBody();
  }

  public void sendCodeForValid(String memberEmail) {
    String code = createCode();
    String text = createText(code);

    // 유효시간 설정 (key, value, 유효시간(분))
    redisUtilService.setDataTimeout(memberEmail, code, 5L);
    sendMail(SendEmailForm.builder()
        .to(memberEmail)
        .from("manager@value.with")
        .subject("트위버 인증코드입니다.")
        .text(text)
        .build());
  }

  public String createCode() {
    return RandomStringUtils.randomAlphanumeric(6);
  }

  public String createText(String code) {
    StringBuilder sb = new StringBuilder();
    sb.append("인증코드를 입력해주세요.\n").append(code);
    return sb.toString();
  }
}
