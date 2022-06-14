package ruby.app.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ruby.app.account.form.*;
import ruby.app.account.repository.AccountRepository;
import ruby.app.account.service.AccountService;
import ruby.app.account.validate.PasswordResetFormValidator;
import ruby.app.account.validate.SignUpFormValidator;
import ruby.app.boards.form.BoardForm;
import ruby.app.boards.form.BoardSearchForm;
import ruby.app.boards.form.SearchOption;
import ruby.app.boards.service.BoardService;
import ruby.app.domain.Account;
import ruby.app.domain.Board;
import ruby.app.util.paging.Paging;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;                            // 계정 서비스
    private final BoardService boardService;                                // 게시글 서비스
    private final SignUpFormValidator signUpFormValidator;                  // 회원가입 검증
    private final PasswordResetFormValidator passwordResetFormValidator;    // 비밀번호 변경 검증
    private final ModelMapper modelMapper;                                   // ModelMapper

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
    public String accountForm(
            @PathVariable Long accountId,
            @ModelAttribute @RequestParam(required = false, defaultValue = "0") int pageNum,
            @LoginAccount Account account, Model model) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException(accountId + "에 해당하는 사용자가 없습니다.");
        }

        Account infoAccount = accountOptional.get();

        if (account != null) {
            model.addAttribute(account);
            model.addAttribute("isOwner", account.equals(infoAccount));
        }

        Page<Board> boards =
                boardService.lookupBoards(pageNum, SearchOption.NICKNAME, infoAccount.getNickname());
        List<BoardForm> boardFormList = boards.stream().map(BoardForm::new).collect(Collectors.toList());
        Paging paging = new Paging().setPagingNumbers(boards.getPageable(), boards.getTotalPages());

        model.addAttribute("infoAccount", infoAccount);
        model.addAttribute("boards", boardFormList);
        model.addAttribute("paging", paging);
        model.addAttribute("optionList", SearchOption.values());

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
        model.addAttribute(modelMapper.map(account, ProfileForm.class));
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

        modelMapper.map(profileForm, account);
        accountService.updateProfile(account);

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

    /**
     * 비밀번호 변경
     * @param account
     * @param passwordResetForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/password-reset")
    public String passwordReset(@LoginAccount Account account,
            @Validated @ModelAttribute("passwordResetForm") PasswordResetForm passwordResetForm, BindingResult bindingResult, Model model) {
        if (account != null) model.addAttribute(account);

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

    /**
     * 비밀번호 찾기 페이지 이동
     * @return
     */
    @GetMapping("/password-forget")
    public String passwordForgetForm(@ModelAttribute("passwordForgetForm") PasswordForgetForm passwordForgetForm) {
        return "account/password-forget";
    }

    /**
     * 비밀번호 찾기 페이지 이동
     * @return
     */
    @PostMapping("/password-forget")
    public String passwordForget(@Validated PasswordForgetForm passwordForgetForm, BindingResult bindingResult, Model model) {
        // 필드 검증
        if (bindingResult.hasErrors()) return "account/password-forget";

        // 이메일 전송
        // TODO - 이메일 인증 처럼 토큰 + url 조합으로 보낸다.
        // /account/password-forget-reset?token=f9872584-dbd0-4d00-9e5b-3f6d6236e3a1&email=ruby@naver.com

        accountService.sendPasswordForgetEmail(passwordForgetForm.getEmail());
        model.addAttribute("sendEmail", true);

        return "account/password-forget";
    }

    /**
     * 이메일에서 비밀번호 변경 링크 클릭시 비밀번호 변경 페이지 이동
     */
    @GetMapping("/password-forget-reset")
    public String passwordForgetResetForm(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);

        if (account == null) {
            // 이메일에 해당하는 계정 정보가 없을 경우
            model.addAttribute("error", "wrong.email");
        } else if (!account.isValidToken(token)) {
            // 입력한 토큰 값이 다른 경우
//            model.addAttribute("error", "wrong.token");
            return "redirect:/";
        } else {
            model.addAttribute("id", account.getId());
        }

        PasswordResetForm passwordResetForm = new PasswordResetForm();
        passwordResetForm.setId(account.getId());
        model.addAttribute(passwordResetForm);

        return "account/password-reset";
    }

    /**
     * 이메일로 보낸 링크를 통한 비밀번호 변경 페이지에서 비밀번호 변경
     * @param passwordResetForm
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/password-forget-reset")
    public String passwordForgetReset(@Validated PasswordResetForm passwordResetForm, BindingResult bindingResult, Model model) {
        // 필드 검증
        if (bindingResult.hasErrors()) return "account/password-reset";

        Optional<Account> optionalAccount = accountRepository.findById(passwordResetForm.getId());
        if (optionalAccount.isEmpty()) return "redirect:/";

        // 비밀번호, 확인용 비밀번호 일치 확인 검증
        Account account = optionalAccount.get();
        passwordResetFormValidator.validate(passwordResetForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "account/password-reset";
        }

        // 비밀번호 변경 적용
        accountService.updatePassword(account, passwordResetForm.getPassword());

        // 변경후 로그인 페이지로 이동
        return "redirect:/login";
    }
}
