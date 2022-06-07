package ruby.app.account.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class PasswordForgetForm {

    @NotBlank
    @Email
    private String email;
}
