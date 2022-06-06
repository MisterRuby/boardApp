package ruby.app.account.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.app.account.form.UserAccount;
import ruby.app.account.repository.AccountRepository;
import ruby.app.domain.Account;

/**
 * DB 에서 유저 정보를 조회하여 인증처리
 */
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    /**
     * username 을 받아 해당하는 정보를 찾아 UserDetails 로 반환
     *      - 사용자가 입력한 정보와 해당 메서드가 반환한 UserDetails 객체의 정보를 비교해서 인증
     *      - AuthenticationManager 의 구현체인 ProviderManager 안에서 호출되어 인증하는데 사용된다.
     * @param email (username)
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);
        if (account == null) throw new UsernameNotFoundException(email);

        // Principal 대상 클래스인 UserAccount 객체 반환
        return new UserAccount(account);
    }
}
