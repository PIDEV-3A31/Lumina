/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.utils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mailer {

    private MimeMessage message;

    public Mailer() throws AddressException {
        try {
            String from = "nourislem.allegui@esprit.tn"; // sender's email address
            String password = "oddq jaza mglc mavg"; // sender's password
            String host = "smtp.gmail.com"; // Gmail SMTP server address
            String port = "465"; // SMTP port

            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.ssl.enable", "true");

            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            this.message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
        } catch (MessagingException ex) {
            Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMail(String to, String subject, String html) throws MessagingException {
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setContent(html, "text/html");
        Transport.send(message);
    }

    public void sendMailAsync(String to, String subject, String html) {
        new Thread(() -> {
            try {
                sendMail(to, subject, html);
            } catch (MessagingException ex) {
                Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

}
