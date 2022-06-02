package ruby.app.account.service;

import ruby.app.domain.Account;

import java.util.Optional;

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
     * @return
     */
    Optional<Account> login(String email, String password);
}
