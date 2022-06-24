package com.smart.tech.start.email;

import com.smart.tech.start.config.MailConfigProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService implements EmailSender{

    private final JavaMailSender mailSender;
    private final MailConfigProperties mailConfigProperties;

    @Override
    @Async
    public void send(String to, String email) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, mailConfigProperties.getEncoding());
            messageHelper.setText(email, true);
            messageHelper.setTo(to);
            messageHelper.setSubject(mailConfigProperties.getSubject());
            messageHelper.setFrom(mailConfigProperties.getSenderEmail());
            mailSender.send(mimeMessage);
        } catch (MessagingException exc) {
            log.error("Failed to send email", exc);
            throw new IllegalStateException("Failed to send email", exc);
        }
    }
}
