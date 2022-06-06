package ruby.app.account.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ruby.app.domain.Account;

/**
 * 계정 Service
 *  - 회원 가입
 *  - 로그인
 *  -
 */
public interface AccountService {

    /**
     * 회원 가입
     * @param email
     * @param nickname
     * @param password
     */
    Account signUp(String email, String nickname, String password);

    /**
     * 로그인
     * @param email
     * @param password
     */
    void login(String email, String password);

    /**
     * 회원 가입 인증 이메일 전송
     * @param account
     */
    void sendSignUpConfirmEmail(Account account);

    /**
     * 이메일 인증 완료처리
     * @param account
     */
    void completeCheckEmail(Account account);

    /**
     * 프로필 변경
     * @param account
     * @param profileImage
     * @param bio
     */
    void updateProfile(Account account, String profileImage, String bio);
}
