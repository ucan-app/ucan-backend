package com.ucan.backend.userauth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  @Value("${MAIL_ENDPOINT_URL}")
  private String mailEndpointUrl;

  private final JavaMailSender mailSender;

  public void sendVerificationEmail(String to, String token, String type)
      throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    String subject = type.equals("USER") ? "Verify Your Account" : "Verify Your Badge";
    String verificationUrl =
        mailEndpointUrl + "/api/auth/verify/" + type.toLowerCase() + "?token=" + token;
    String content =
        String.format(
            "Please click the following link to verify your %s: %s",
            type.equals("USER") ? "account" : "badge", verificationUrl);

    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(content);

    mailSender.send(message);
  }
}
