package ruby.app.util.mail.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ruby.app.util.mail.EmailMessage;
import ruby.app.util.mail.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class HtmlEmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    /**
     * 이메일 전송
     * @param emailMessage
     */
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailMessage.getMessage(), true);        // boolean 값은 메시지를 html 형태로 보낼 것인가에 대한 여부
            mailSender.send(mimeMessage);
            log.info("send email: {}", emailMessage.getMessage());
        } catch (MessagingException e) {
            log.error("failed to send email", e);
        }
    }
}
