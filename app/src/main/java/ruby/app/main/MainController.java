package ruby.app.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ruby.app.account.form.LoginAccount;
import ruby.app.domain.Account;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String home(@LoginAccount Account account, Model model) {
        if (account != null) model.addAttribute(account);

        return "index";
    }

    /**
     * 로그인 페이지 이동
     * @return
     */
    @GetMapping("/login")
    public String loginForm() {
        return "account/login";
    }

    /**
     * 로그아웃
     * @return
     */
    @GetMapping("/logout")
    public String logout() {
        // TODO - 로그아웃 처리 후 메인 페이지 이동
        return "redirect:/";
    }
}
