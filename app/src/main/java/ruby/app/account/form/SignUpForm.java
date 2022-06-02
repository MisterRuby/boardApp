package ruby.app.account.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SignUpForm {

    @NotBlank @Email
    private String email;
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{2,20}$",
            message = "2~20자 한글, 영문 대소문자, 숫자만 입력할 수 있습니다.")
    private String nickname;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,50}$",
            message = "8~50자 영문 대소문자, 숫자, 특수문자만 입력할 수 있습니다. 비밀번호는 적어도 하나 이상의 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;
}
