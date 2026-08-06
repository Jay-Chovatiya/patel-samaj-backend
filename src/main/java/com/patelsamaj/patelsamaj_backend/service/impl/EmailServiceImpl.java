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

        String body = "જય શ્રી કૃષ્ણ, " + event.getFamilyHeadName() + ",\n\n"
                + "પટેલ સમાજ સાથે બુકિંગ કરવા બદલ આપનો ખૂબ ખૂબ આભાર. આપની ઇવેન્ટ (" + event.getEventType() + ") નું બિલ સફળતાપૂર્વક ક્લોઝ કરી દેવામાં આવ્યું છે.\n\n"
                + "ઇવેન્ટ શરૂઆતની તારીખ: " + event.getStartDate() + "\n"
                + "ઇવેન્ટ સમાપ્તિ તારીખ: " + event.getEndDate() + "\n"
                + "કુલ ચૂકવવાપાત્ર રકમ: ₹" + event.getTotalAmount() + "\n\n"
                + "આ બિલ હવે ડિજિટલ સિસ્ટમમાં ફાઇનલ કરીને લોક કરી દેવામાં આવ્યું છે, તેથી હવે આમાં કોઈ ફેરફાર થઈ શકશે નહીં. જો કોઈ પ્રશ્ન હોય તો સમાજના કાર્યાલયનો સંપર્ક કરવા વિનંતી.\n\n"
                + "શુભેચ્છા સહ,\n"
                + "પટેલ સમાજ મેનેજમેન્ટ";

        sendMail(event.getEmail(), "પટેલ સમાજ - ફાઇનલ બિલ રસીદ", body);
    }


    @Async
    public void sendResetPasswordEmail(String toEmail, String plainTextPassword, String userName) {
        String body = "જય શ્રી કૃષ્ણ, " + userName + ",\n\n"
                + "અમને તમારો પાસવર્ડ રીસેટ કરવા માટેની વિનંતી મળી છે. તમારા લોગિન માટેનો નવો પાસવર્ડ નીચે મુજબ છે:\n\n"
                + "નવો પાસવર્ડ (New Password): " + plainTextPassword + "\n\n"
                + "તમારી સુરક્ષા માટે, કૃપા કરીને તરત જ લોગિન કરો અને આ પાસવર્ડ બદલીને તમને યાદ રહે તેવો નવો પાસવર્ડ સેટ કરી લો.\n\n"
                + "શુભેચ્છા સહ,\nપટેલ સમાજ મેનેજમેન્ટ";

        sendMail(toEmail, "પટેલ સમાજ - પાસવર્ડ રીસેટ", body);
    }

    @Async
    public void sendWelcomeEmail(String toEmail, String userName, String mobileNumber, String plainTextPassword) {
        String body = "જય શ્રી કૃષ્ણ, " + userName + ",\n\n"
                + "પટેલ સમાજ મેનેજમેન્ટ પોર્ટલ પર આપનું હાર્દિક સ્વાગત છે! આપનું એકાઉન્ટ સફળતાપૂર્વક બનાવી દેવામાં આવ્યું છે.\n\n"
                + "આપની લોગિન વિગતો નીચે મુજબ છે:\n"
                + "યુઝરનેમ (Username): " + mobileNumber + "\n"
                + "પાસવર્ડ (Password): " + plainTextPassword + "\n\n"
                + "પોર્ટલમાં લોગિન કરવા માટે આપના કોન્ટેક્ટ નંબર (મોબાઈલ નંબર) નો ઉપયોગ કરો. સુરક્ષા માટે, કૃપા કરીને પ્રથમ વખત લોગિન કર્યા પછી આ પાસવર્ડ બદલી નાખવો.\n\n"
                + "હવે આપ સમાજની અપડેટ્સ, ઇવેન્ટ બુકિંગ અને બિલ્ડિંગ ફંડના દાન (Contributions) ની વિગતો ઓનલાઈન જોઈ શકશો.\n\n"
                + "શુભેચ્છા સહ,\n"
                + "પટેલ સમાજ મેનેજમેન્ટ કમિટી";

        sendMail(toEmail, "પટેલ સમાજ પોર્ટલ પર આપનું સ્વાગત છે", body);
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