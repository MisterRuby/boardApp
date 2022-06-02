package ruby.app.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ruby.app.account.form.SignUpForm;
import ruby.app.account.service.AccountService;
import ruby.app.account.util.validate.SignUpFormValidator;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;                // 계정 서비스
    private final SignUpFormValidator signUpFormValidator;      // 회원가입 검증

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
    public String signUpForm(@ModelAttribute("signUpForm") SignUpForm signUpForm) {
        return "account/sign-up";
    }

    /**
     * 회원가입
     * @return
     */
    @PostMapping("/account/sign-up")
    public String signUp(@Validated @ModelAttribute("signUpForm") SignUpForm signUpForm, BindingResult bindingResult) {
        // 필드 검증
        if (bindingResult.hasErrors()) return "account/sign-up";

        // 이메일, 닉네임 중복 검증
        signUpFormValidator.validate(signUpForm, bindingResult);
        if (bindingResult.hasErrors()) return "account/sign-up";

        accountService.signUp(signUpForm.getEmail(), signUpForm.getNickname(), signUpForm.getPassword());

        return "redirect:/boards";
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
