package ruby.app.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.FetchType.EAGER;

/**
 * 계정 엔티티
 */
@Entity
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Account {

    /** 컬럼 */
    @Id @GeneratedValue
    @Column(name = "account_id")
    private Long id;
    @Column(unique = true)
    private String email;                           // 가입 email
    @Column(unique = true)
    private String nickname;                        // 닉네임
    private String password;                        // 비밀번호
    private boolean emailVerified;                  // email 인증 여부
    private String emailCheckToken;                 // email 인증 시 사용할 토큰 값
    private LocalDateTime joinedAt;                 // 가입일자 (인증 확인 시점)



    /** 비즈니스 메서드 */

    /**
     * 이메일 인증 토큰 생성
     * @return
     */
    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }
}
