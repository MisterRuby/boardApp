package ruby.app.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ruby.app.account.form.SignUpForm;

@Slf4j
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
    public String signUpForm(@ModelAttribute("account") SignUpForm account) {
        return "account/sign-up";
    }

    // 회원가입 페이지에서 회원정보 입력 -> 가입 -> 메일 인증 -> 글쓰기 가능
    /**
     * 회원가입
     * @return
     */
    @PostMapping("/account/sign-up")
    public String signUp(@Validated @ModelAttribute("account") SignUpForm signUpForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "account/sign-up";

        log.info("email={} / nickname={} / password={}",
                signUpForm.getEmail(), signUpForm.getNickname(), signUpForm.getPassword());

        /*
            TODO - 회원 정보 저장. 저장 후 게시글 목록 페이지로 이동.
                 이메일 인증 처리 구현 필요
         */
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
