package com.patelsamaj.patelsamaj_backend.service;

import com.patelsamaj.patelsamaj_backend.model.Event;

public interface EmailService {
    void sendBillReceipt(Event event);
    void sendNewPasswordEmail(String toEmail, String plainTextPassword, String userName);
}
