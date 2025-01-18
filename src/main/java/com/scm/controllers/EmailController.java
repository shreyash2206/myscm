package com.scm.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.scm.entities.Contact;
import com.scm.services.ContactService;
import com.scm.services.EmailService;

import jakarta.mail.MessagingException;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ContactService contactService;

    // Show the email composition form
    @GetMapping("/user/compose-email")
    public String composeEmail(@RequestParam("contactId") String contactId, Model model) {
        Contact contact = contactService.getById(contactId);
        model.addAttribute("contact", contact);
        return "user/compose-email"; // Returns the email composition form
    }

    // Handle sending the email
    @PostMapping("/user/send-email")
    public String sendEmail(@RequestParam("contactId") String contactId,
                            @RequestParam("subject") String subject,
                            @RequestParam("body") String body,
                            @RequestParam(value = "attachment", required = false) MultipartFile attachment,
                            Model model) {
        Contact contact = contactService.getById(contactId);

        try {
            // Send email with or without an attachment
            if (attachment != null && !attachment.isEmpty()) {
                emailService.sendEmailWithAttachment(contact.getEmail(), subject, body, attachment);
            } else {
                emailService.sendEmail(contact.getEmail(), subject, body);
            }
            model.addAttribute("message", "Email sent successfully to " + contact.getEmail());
        } catch (MessagingException e) {
            model.addAttribute("error", "Failed to send email. Please try again.");
            e.printStackTrace(); // Log error for debugging
        } catch (Exception ex) {
            model.addAttribute("error", "An unexpected error occurred while sending the email.");
            ex.printStackTrace();
        }

        // Redirect back to contact list or email page
        return "redirect:/user/contacts";  
    }
  
    }

