package com.greb.pothubbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSenderImpl mailSender;

    public void sendMail(String to, String subject, String content) {
        try{
            var message = mailSender.createMimeMessage();
            System.out.println("Message "+message);
            var helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("PotHub");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        }
        catch(Exception e){
            log.error("Unable to send email to {}",to);
        }
    }
}
