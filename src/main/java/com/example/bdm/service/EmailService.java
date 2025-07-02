package com.example.bdm.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${app.domain}")
    private String appDomain;
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendRegistrationActivationEmail(String userEmail, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("noreply@bdm.com");
        helper.setTo(userEmail);
        helper.setSubject("Registration Validation Link");

        String activationLink = appDomain + "/api/auth/"

        String htmlContent = "<html>" +
                "<body>" +
                "<h2>Welcome!</h2>" +
                "<p>Thanks for registering. Please click the link below to fully activate your account:</p>" +
                "<a href=\"" + activationLink + "\">Activate Account</a>" +
                "<br/><br/>" +
                "<p>If you did not register, please ignore this email.</p>" +
                "</body>" +
                "</html>";

        helper.setText(htmlContent, true); // true = isHtml

        mailSender.send(message);
    }
}
