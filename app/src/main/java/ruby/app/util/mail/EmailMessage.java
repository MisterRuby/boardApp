package ruby.app.util.mail;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class EmailMessage {

    private String to;              // 이메일을 보낼 대상
    private String subject;         // 이메일 제목
    private String message;         // 메시지
}
