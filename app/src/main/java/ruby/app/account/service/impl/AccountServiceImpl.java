package ruby.app.account.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ruby.app.account.repository.AccountRepository;
import ruby.app.account.service.AccountService;
import ruby.app.domain.Account;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;          // 계정 Repository
    private final JavaMailSender mailSender;                    // 메일 sender - 콘솔 테스트 용
    private final PasswordEncoder passwordEncoder;              // PasswordEncoder;

    /**
     * 회원 가입
     * @param email
     * @param nickname
     * @param password
     */
    @Override
    public Account signUp(String email, String nickname, String password) {
        Account newAccount = saveNewAccount(email, nickname, password);
        newAccount.generateEmailCheckToken();   // 이메일 인증 토큰 생성
        sendSignUpConfirmEmail(newAccount);     // 회원 가입 인증 이메일 전송

        return newAccount;
    }

    /**
     * 로그인
     * @param email
     * @param password
     * @return
     */
    @Override
    public Optional<Account> login(String email, String password) {
        return Optional.empty();
    }





    /**
     * 신규 계정 정보 저장
     * @param email
     * @param nickname
     * @param password
     * @return
     */
    private Account saveNewAccount(String email, String nickname, String password) {
        Account account = Account.builder()
                .email(email)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .build();

        return accountRepository.save(account);
    }

    /**
     * 회원 가입 인증 이메일 전송
     * @param newAccount
     */
    private void sendSignUpConfirmEmail(Account newAccount) {
        String token = newAccount.getEmailCheckToken();
        String email = newAccount.getEmail();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());                                            // 메일을 보낼 대상 이메일 주소
        mailMessage.setSubject("Ruby's Board, 회원 가입 인증");                                  // 메일 제목
        mailMessage.setText("/check-email-token?token=" + token + "&email=" + email);        // 메일 본문
        mailSender.send(mailMessage);
    }
}
