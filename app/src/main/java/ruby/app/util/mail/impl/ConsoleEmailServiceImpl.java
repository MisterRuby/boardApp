package ruby.app.util.mail.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ruby.app.util.mail.EmailMessage;
import ruby.app.util.mail.EmailService;

@Slf4j
@Profile("local")
@Service
public class ConsoleEmailServiceImpl implements EmailService {

    /**
     * 이메일 전송 - 콘솔 테스트
     * @param emailMessage
     */
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        log.info("send email: {}", emailMessage.getMessage());
    }
}
