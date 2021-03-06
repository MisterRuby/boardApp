package ruby.app.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ruby.app.account.form.LoginAccount;
import ruby.app.account.form.LoginForm;
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
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "account/login";
    }

}
