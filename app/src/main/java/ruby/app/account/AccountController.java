package ruby.app.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ruby.app.account.form.LoginAccount;
import ruby.app.account.form.PasswordResetForm;
import ruby.app.account.form.ProfileForm;
import ruby.app.account.form.SignUpForm;
import ruby.app.account.repository.AccountRepository;
import ruby.app.account.service.AccountService;
import ruby.app.account.validate.PasswordResetFormValidator;
import ruby.app.account.validate.SignUpFormValidator;
import ruby.app.domain.Account;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;                            // 계정 서비스
    private final SignUpFormValidator signUpFormValidator;                  // 회원가입 검증
    private final PasswordResetFormValidator passwordResetFormValidator;    // 비밀번호 변경 검증

    /**
     * 회원가입 페이지 이동
     * @return
     */
    @GetMapping("/sign-up")
    public String signUpForm(@ModelAttribute("signUpForm") SignUpForm signUpForm) {
        return "account/sign-up";
    }

    /**
     * 회원가입
     * @return
     */
    @PostMapping("/sign-up")
    public String signUp(@Validated @ModelAttribute("signUpForm") SignUpForm signUpForm, BindingResult bindingResult) {
        // 필드 검증
        if (bindingResult.hasErrors()) return "account/sign-up";

        // 이메일, 닉네임 중복 검증
        signUpFormValidator.validate(signUpForm, bindingResult);
        if (bindingResult.hasErrors()) return "account/sign-up";

        Account newAccount = accountService.signUp(signUpForm.getEmail(), signUpForm.getNickname(), signUpForm.getPassword());
        accountService.login(newAccount.getEmail(), newAccount.getPassword());

        return "redirect:/";
    }

    /**
     * 이메일 인증 처리
     *  - 메시지로 보낸 인증 링크를 이메일에서 누르면 해당 핸들러를 요청
     * @param token
     * @param email
     * @param model
     * @return
     */
    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);

        if (account == null) {
            // 이메일에 해당하는 계정 정보가 없을 경우
            model.addAttribute("error", "wrong.email");
        } else if (!account.isValidToken(token)) {
            // 입력한 토큰 값이 다른 경우
            model.addAttribute("error", "wrong.token");
        } else {
            accountService.completeCheckEmail(account);
            model.addAttribute(account);
        }

        return "account/checked-email";
    }

    /**
     * 이메일 인증 재전송 페이지 이동
     * @param account
     * @param model
     * @return
     */
    @GetMapping("/check-email")
    public String checkEmailForm(@LoginAccount Account account, Model model) {
        model.addAttribute("account", account);
        return "/account/check-email";
    }

    /**
     * 이메일 인증 재전송 요청
     * @param account
     * @return
     */
    @GetMapping("/resend-confirm-email")
    @ResponseBody
    public void resendConfirmEmail(@LoginAccount Account account) {
        accountService.sendSignUpConfirmEmail(account);
    }

    /**
     * 계정 정보 조회
     * @param account
     * @param model
     * @return
     */
    @GetMapping("/{accountId}")
    public String accountForm(@PathVariable Long accountId, @LoginAccount Account account, Model model) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException(accountId + "에 해당하는 사용자가 없습니다.");
        }

//        model.addAttribute("account", accountOptional.get());
        model.addAttribute(accountOptional.get());      // 생략할 경우 매개변수의 객체 타입명의 캐멀케이스 문자열 값이 키 값이 된다.
        model.addAttribute("isOwner", account.equals(accountOptional.get()));

        return "account/profile";
    }

    /**
     * 프로필 수정 페이지 이동
     * @param account
     * @param model
     * @return
     */
    @GetMapping("/profile")
    public String editProfileForm(@LoginAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new ProfileForm(account));
        return "account/editProfile";
    }

    /**
     * 프로필 수정
     * @param account
     * @param profileForm
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/profile")
    public String editProfile(@LoginAccount Account account, @Validated ProfileForm profileForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            return "account/editProfile";
        }

        accountService.updateProfile(account, profileForm.getProfileImage(), profileForm.getBio());

        return "redirect:/account/" + account.getId();
    }

    /**
     * 비밀번호 변경 페이지 이동
     * @return
     */
    @GetMapping("/password-reset")
    public String passwordResetForm(@ModelAttribute("passwordResetForm") PasswordResetForm passwordResetForm) {
        return "account/password-reset";
    }

    @PostMapping("/password-reset")
    public String passwordReset(@LoginAccount Account account,
            @Validated @ModelAttribute("passwordResetForm") PasswordResetForm passwordResetForm, BindingResult bindingResult) {
        // 필드 검증
        if (bindingResult.hasErrors()) return "account/password-reset";

        // 비밀번호, 확인용 비밀번호 일치 확인 검증
        passwordResetFormValidator.validate(passwordResetForm, bindingResult);
        if (bindingResult.hasErrors()) return "account/password-reset";

        // 비밀번호 변경 적용
        accountService.updatePassword(account, passwordResetForm.getPassword());

        // 변경후 프로필 페이지로 이동
        return "redirect:/account/" + account.getId();
    }
}
