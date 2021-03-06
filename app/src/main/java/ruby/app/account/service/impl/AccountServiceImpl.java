package ruby.app.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ruby.app.account.form.UserAccount;
import ruby.app.account.repository.AccountRepository;
import ruby.app.account.service.AccountService;
import ruby.app.config.AppProperties;
import ruby.app.domain.Account;
import ruby.app.util.mail.EmailMessage;
import ruby.app.util.mail.EmailService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;          // 계정 Repository
    private final EmailService emailService;                    // emailService
    private final PasswordEncoder passwordEncoder;              // PasswordEncoder
    private final TemplateEngine templateEngine;                // templateEngine
    private final AppProperties appProperties;                  // appProperties

    /**
     * 회원 가입
     * @param email
     * @param nickname
     * @param password
     */
    @Override
    public Account signUp(String email, String nickname, String password) {
        Account newAccount = saveNewAccount(email, nickname, password);
//        newAccount.generateEmailCheckToken();   // 이메일 인증 토큰 생성
        sendSignUpConfirmEmail(newAccount);     // 회원 가입 인증 이메일 전송 - 토큰을 이메일 전송시 새로 생성

        return newAccount;
    }

    /**
     * 로그인
     * @param email
     * @param password
     */
    @Override
    @Transactional(readOnly = true)
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
        // 새로운 토큰을 발행해서 보내도록 변경
        account.generateEmailCheckToken();
        accountRepository.save(account);

        String token = account.getEmailCheckToken();
        String email = account.getEmail();

        Context context = new Context();
        context.setVariable("link", "/account/check-email-token?token=" + token + "&email=" + email);
        context.setVariable("nickname", account.getNickname());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "이메일 인증을 완료하려면 하단 링크를 클릭하세요.");
        context.setVariable("host", appProperties.getHost());

        String htmlMessage = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(email)
                .subject("Ruby's Board, 회원 가입 인증")
                .message(htmlMessage)
                .build();

        emailService.sendEmail(emailMessage);
    }

    /**
     * 이메일 인증 완료처리
     * @param account
     */
    @Override
    public void completeCheckEmail(Account account) {
        account.checkEmailSuccess();
        accountRepository.save(account);
        login(account.getEmail(), account.getPassword());
    }


    /**
     * 프로필 변경
     * @param account
     */
    @Override
    public void updateProfile(Account account) {
//        if (profileImage != null && !profileImage.isBlank()) account.setProfileImage(profileImage);
//        if (bio != null && !bio.isBlank()) account.setBio(bio);

        /*
             account 는 세션에 저장된 객체로 이미 한 번 조회했지만 엔티티 매니저가 관리하지 않는 준영속(detach) 상태의 엔티티다.
             - 준영속 엔티티는 변경감지는 일어나지 않지만 save 를 통해 변경된 내용을 DB에 업데이트 할 수 있다. (id 값이 있기 때문에)
        */
        accountRepository.save(account);
    }

    /**
     * 비밀번호 변경
     * @param account
     * @param password
     */
    @Override
    public void updatePassword(Account account, String password) {
        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
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
     * 비밀번호 변경을 위한 링크를 이메일로 보내기
     * @param email
     */
    @Override
    public void sendPasswordForgetEmail(String email) {
        Account account = accountRepository.findByEmail(email);

        if (account == null) throw new IllegalArgumentException(email + "에 해당하는 사용자가 없습니다.");

        account.generateEmailCheckToken();
        String token = account.getEmailCheckToken();

        Context context = new Context();
        context.setVariable("link", "/account/password-forget-reset?token=" + token + "&email=" + email);
        context.setVariable("nickname", account.getNickname());
        context.setVariable("linkName", "비밀번호 변경하기");
        context.setVariable("message", "비밀번호를 변경하려면 하단 링크를 클릭하세요.");
        context.setVariable("host", appProperties.getHost());

        String htmlMessage = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("Ruby's Board, 비밀번호 변경하기")
                .message(htmlMessage)
                .build();

        emailService.sendEmail(emailMessage);
    }
}
