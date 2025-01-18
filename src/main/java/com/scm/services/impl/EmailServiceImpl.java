package com.scm.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scm.services.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.properties.domain_name}")
    private String domainName;

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(domainName);
        emailSender.send(message);
    }

    @Override
    public void sendEmailWithHtml(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // `true` enables HTML content
        helper.setFrom(domainName);

        emailSender.send(mimeMessage);
    }


    @Override
    public void sendEmailWithAttachment(String to, String subject, String body, MultipartFile attachment) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true for multipart
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);
        helper.setFrom(domainName);
    
        // Add the attachment
        try {
            helper.addAttachment(attachment.getOriginalFilename(), attachment);
        } catch (MessagingException e) {
            throw new MessagingException("Error while attaching file", e);
        }
    
        emailSender.send(message);
    }

}
