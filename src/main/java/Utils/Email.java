package Utils;



import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Email implements EmailAdapter{

    final private String emailFrom = "asfandtorp@gmail.com";
    final private String password = "sH6Zg21YZ83JHz";
    final private String host = "smtp.gmail.com";

    @Override
    public boolean sendEmail(String mailTo, String mailCc, String mailSubject, String mailText) {
        try {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.user", emailFrom);
            properties.put("mail.smtp.password", password);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            emailSession.setDebug(true);

            Message emailMessage = new MimeMessage(emailSession);
            emailMessage.setFrom(new InternetAddress(emailFrom));
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
            if(mailCc != null)emailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(mailCc));
            emailMessage.setSubject(mailSubject);
            emailMessage.setText(mailText);

            Transport transport = emailSession.getTransport("smtp");
            transport.connect(host, emailFrom, password);
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();

            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }

}
