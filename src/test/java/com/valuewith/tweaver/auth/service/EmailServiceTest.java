package com.valuewith.tweaver.auth.service;

import com.valuewith.tweaver.auth.client.mailgun.SendEmailForm;
import java.util.StringTokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {

  static {
    System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
  }

  @Autowired
  private EmailService emailService;

  @Test
  void success_email_test() throws Exception {
    //given
    SendEmailForm sendEmailForm = SendEmailForm.builder()
        .from("tweaverTest@gmail.com")
        .to("eod940@gmail.com")
        .subject("Test Email")
        .text("Hello Email")
        .build();

    //when
    /**
     * sendMail 호출이 성공적으로 이루어질 경우 response 값(예시 값):
     *
     * {"id": "<20111114174239.25659.5817@samples.mailgun.org>","message": "Queued. Thank you."}\n
     *
     */
    String response = emailService.sendMail(sendEmailForm);

    //then
    // StringTokenizer를 이용해 응답값의 "message..." 부분만 추출해 성공인지 확인
    StringTokenizer st = new StringTokenizer(response, ",");
    for (int i = 1; i <= 2; i++) {
      response = st.nextToken();
    }
    Assertions.assertEquals(response, "\"message\":\"Queued. Thank you.\"}\n");
  }
}