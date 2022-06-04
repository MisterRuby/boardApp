package ruby.app.account.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.app.account.form.UserAccount;
import ruby.app.account.repository.AccountRepository;
import ruby.app.account.service.AccountService;
import ruby.app.domain.Account;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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
     */
    @Override
    public void login(String email, String password) {
        Account account = accountRepository.findByEmail(email);

        // UsernamePasswordAuthenticationToken 에서 첫번째 인자로 지정한 값이 principal 객체가 된다.
        // 해당 클래스의 생성자 메서드 참고해서 볼 것
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        new UserAccount(account), password, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    /**
     * 회원 가입 인증 이메일 전송
     * @param account
     */
    @Override
    public void sendSignUpConfirmEmail(Account account) {
        String token = account.getEmailCheckToken();
        String email = account.getEmail();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(account.getEmail());                                            // 메일을 보낼 대상 이메일 주소
        mailMessage.setSubject("Ruby's Board, 회원 가입 인증");                                  // 메일 제목
        mailMessage.setText("/account/check-email-token?token=" + token + "&email=" + email);        // 메일 본문
        mailSender.send(mailMessage);
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
}
