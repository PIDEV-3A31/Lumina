package come.esprit.services;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.util.Properties;

public class MailService {
    private static final String senderEmail = "zidiiali20@gmail.com";
    private static final String senderPassword = "zfpd xxxi jouh hgtz";

    public static void sendEmailWithAttachment(String recipient, String subject, String messageText, String filePath) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            // Partie texte
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(messageText);

            // Partie pièce jointe
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(new File(filePath));
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(new File(filePath).getName());

            // Création du message final avec plusieurs parties
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // Envoi de l'e-mail
            Transport.send(message);
            System.out.println("E-mail envoyé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
