package com.valuewith.tweaver.auth.service;

import com.valuewith.tweaver.auth.client.MailgunClient;
import com.valuewith.tweaver.auth.client.mailgun.SendEmailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final MailgunClient mailgunClient;

  public String sendMail(SendEmailForm sendForm) {
    return mailgunClient.sendEmail(sendForm).getBody();
  }
}
