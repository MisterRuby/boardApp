package ruby.app.account.validate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ruby.app.account.form.SignUpForm;
import ruby.app.account.repository.AccountRepository;

/**
 * 회원 가입시 추가 검증을 위한 Validator
 */
@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    /**
     * 회원 가입시 이메일, 닉네임 중복 여부 확인
     */
    @Override
    public void validate(Object target, Errors errors) {
        String email = ((SignUpForm) target).getEmail();
        boolean existsByEmail = accountRepository.existsByEmail(email);
        if (existsByEmail) {
            errors.rejectValue("email", "invalid.email", new Object[]{email}, "이미 등록되어 있는 이메일입니다.");
        }

        String nickname = ((SignUpForm) target).getNickname();
        boolean existsByNickname = accountRepository.existsByNickname(nickname);
        if (existsByNickname) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{nickname}, "이미 사용중인 닉네임입니다.");
        }
    }
}
