package com.patelsamaj.patelsamaj_backend.service;

import com.patelsamaj.patelsamaj_backend.model.Event;

public interface EmailService {
    void sendBillReceipt(Event event);
    void sendResetPasswordEmail(String toEmail, String plainTextPassword, String userName);
    void sendWelcomeEmail(String toEmail, String userName, String mobileNumber, String plainTextPassword);
}
