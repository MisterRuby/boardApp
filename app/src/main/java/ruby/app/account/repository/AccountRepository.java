package ruby.app.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ruby.app.domain.Account;

import java.util.Optional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * 이메일 중복 확인
     * @param email
     * @return
     */
    boolean existsByEmail(String email);

    /**
     * 닉네임 중복 확인
     * @param nickname
     * @return
     */
    boolean existsByNickname(String nickname);

    /**
     * 이메일로 회원 정보 조회
     * @param email
     * @return
     */
    Optional<Account> findByEmail(String email);
}
