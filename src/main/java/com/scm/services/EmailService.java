package com.scm.services;

import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendEmail(String to, String subject, String body) throws MessagingException;

    void sendEmailWithHtml(String to, String subject, String body) throws MessagingException;

    void sendEmailWithAttachment(String to, String subject, String body, MultipartFile attachment) throws MessagingException;

}
