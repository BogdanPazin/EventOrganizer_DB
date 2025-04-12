import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailSend {
    public static void sendEmail(String to, String subject, String content) {
        final String fromEmail = "eventInfo@gmail.com";
        final String password = "event123";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}