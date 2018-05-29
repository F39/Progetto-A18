package Utils;

public interface EmailAdapter {
    boolean sendEmail(String mailTo, String mailCc, String mailSubject, String mailText);
}
