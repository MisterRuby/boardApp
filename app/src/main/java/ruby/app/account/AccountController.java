package ruby.app.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    /**
     * 로그인 페이지 이동
     * @return
     */
    @GetMapping("/account/login")
    public String loginForm() {
        return "account/login";
    }

    /**
     * 회원가입 페이지 이동
     * @return
     */
    @GetMapping("/account/sign-up")
    public String signUpForm() {
        return "account/sign-up";
    }

    /**
     * 비밀번호 변경 페이지 이동
     * @return
     */
    @GetMapping("/account/password-reset")
    public String passwordResetForm() {
        return "account/password-reset";
    }
}
