package ruby.app.account.validate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ruby.app.account.form.PasswordResetForm;

@Component
@RequiredArgsConstructor
public class PasswordResetFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PasswordResetForm.class);
    }

    /**
     * 비밀번호 변경시 비밀번호와 확인용 비밀번호가 일치하는지 확인
     */
    @Override
    public void validate(Object target, Errors errors) {
        PasswordResetForm passwordResetForm = (PasswordResetForm) target;
        String password = passwordResetForm.getPassword();
        String passwordConfirm = passwordResetForm.getPasswordConfirm();

        if (!password.equals(passwordConfirm)) {
            errors.rejectValue("passwordConfirm", "wrong.passwordConfirm",
                    new Object[]{passwordConfirm}, "입력한 새 패스워드가 일치하지 않습니다.");
       }
    }
}
