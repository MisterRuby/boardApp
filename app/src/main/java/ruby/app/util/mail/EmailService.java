package ruby.app.util.mail;

public interface EmailService {

    /**
     * 이메일 전송
     * @param emailMessage
     */
    void sendEmail(EmailMessage emailMessage);
}
