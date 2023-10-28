package com.valuewith.tweaver.auth.client;

import com.valuewith.tweaver.auth.client.mailgun.SendEmailForm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun", url = "https://api.mailgun.net/v3/")
@Qualifier("mailgun")
public interface MailgunClient {

  /**
   * mailgun에서 QueryString을 보내기 때문에 @SpringQueryMap을 사용
   */
  @PostMapping("${mailgun-domain}")
  ResponseEntity<String> sendEmail(@SpringQueryMap SendEmailForm form);
}
