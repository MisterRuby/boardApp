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
}