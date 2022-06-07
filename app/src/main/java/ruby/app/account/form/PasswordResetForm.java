package ruby.app.account.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class PasswordResetForm {

    private Long id;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,50}$",
            message = "8~50자 영문, 숫자, 특수문자만 입력할 수 있습니다. 비밀번호는 적어도 하나 이상의 영문, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,50}$",
            message = "8~50자 영문, 숫자, 특수문자만 입력할 수 있습니다. 비밀번호는 적어도 하나 이상의 영문, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String passwordConfirm;
}
