package com.patelsamaj.patelsamaj_backend.service.impl;

import com.patelsamaj.patelsamaj_backend.model.Event;
import com.patelsamaj.patelsamaj_backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendBillReceipt(Event event) {
        if (event.getEmail() == null || event.getEmail().isBlank()) {
            return;
        }

        String body = "Jai Shree Krishna, " + event.getFamilyHeadName() + ",\n\n"
                + "Thank you for booking with Patel Samaj. Your event (" + event.getEventType() + ") bill has been successfully closed.\n\n"
                + "Event Date: " + event.getEventDate().toLocalDate() + "\n"
                + "Total Amount: ₹" + event.getTotalAmount() + "\n\n"
                + "This bill is now finalized and locked in our system. Please contact the Samaj administration if you have any questions.\n\n"
                + "Warm Regards,\n"
                + "Patel Samaj Management";

        sendMail(event.getEmail(), "Patel Samaj - Final Bill Receipt for " + event.getFamilyHeadName(), body);
    }

    @Async
    public void sendNewPasswordEmail(String toEmail, String plainTextPassword, String userName) {
        String body = "Jai Shree Krishna, " + userName + ",\n\n"
                + "We received a request to reset your password. Here are your new login details:\n\n"
                + "New Password: " + plainTextPassword + "\n\n"
                + "For your security, please log in immediately and change this password to something you will remember.\n\n"
                + "Warm Regards,\nPatel Samaj Management";
        sendMail(toEmail, "Patel Samaj - Password Reset", body);
    }

    private void sendMail(String toEmail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}