package ruby.app.account;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ruby.app.account.repository.AccountRepository;
import ruby.app.account.service.AccountService;
import ruby.app.domain.Account;
import ruby.app.util.mail.EmailMessage;
import ruby.app.util.mail.EmailService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@Rollback
@ActiveProfiles("dev")
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @MockBean
    EmailService emailService;

    // ????????? ?????? - "rubykim0723@gmail.com", "ruby12", "123!@#qweQWE"

    @BeforeAll
    void addAdmin() {
        Mockito.reset(emailService);
    }

    @Test
    @DisplayName("index ?????????")
    void index() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("???????????? ?????? ?????????")
    void signUpForm() throws Exception {
        mockMvc.perform(get("/account/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @Test
    @DisplayName("???????????? ?????? - ????????? ??????")
    void validateSignUpEmailError() throws Exception {
        mockMvc.perform(
                post("/account/sign-up")
                        .param("email", "adacf")
                        .param("nickname", "ruby1")
                        .param("password", "adaASD12$$")
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeHasFieldErrorCode("signUpForm", "email", "Email"));
    }

    @Test
    @DisplayName("???????????? ?????? - ????????? ??????")
    void validateSignUpNicknameError() throws Exception {
        mockMvc.perform(
                        post("/account/sign-up")
                                .param("email", "ruby1@naver.com")
                                .param("nickname", "r")
                                .param("password", "adaASD12$$")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeHasFieldErrorCode("signUpForm", "nickname", "Pattern"));
    }

    @Test
    @DisplayName("???????????? ?????? - ???????????? ??????")
    void validateSignUpPasswordError() throws Exception {
        mockMvc.perform(
                        post("/account/sign-up")
                                .param("email", "ruby1@naver.com")
                                .param("nickname", "ruby1")
                                .param("password", "adadfg")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeHasFieldErrorCode("signUpForm", "password", "Pattern"));
    }

    @Test
    @DisplayName("???????????? ?????? - ????????? ?????? ??????")
    void validateSignUpAllFieldError() throws Exception {
        mockMvc.perform(
                        post("/account/sign-up")
                                .param("email", "adacf")
                                .param("nickname", "r")
                                .param("password", "adadfg")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeHasFieldErrorCode("signUpForm", "email", "Email"))
                .andExpect(model().attributeHasFieldErrorCode("signUpForm", "nickname", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("signUpForm", "password", "Pattern"));
    }

    @Test
    @DisplayName("???????????? ?????? - ????????? ?????? ??????")
    void validateExistsEmail() throws Exception {
        mockMvc.perform(
                        post("/account/sign-up")
                                .param("email", "rubykim0723@gmail.com")
                                .param("nickname", "ruby141221")
                                .param("password", "adaASD12$$")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeHasFieldErrorCode("signUpForm", "email", "invalid.email"));
    }

    @Test
    @DisplayName("???????????? ?????? - ????????? ?????? ??????")
    void validateExistsNickname() throws Exception {
        mockMvc.perform(
                        post("/account/sign-up")
                                .param("email", "ruby123@naver.com")
                                .param("nickname", "ruby12")
                                .param("password", "adaASD12$$")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeHasFieldErrorCode("signUpForm", "nickname", "invalid.nickname"));
    }

    @Test
    @DisplayName("???????????? ?????? - ????????? ?????? ??????")
    void validateSignUp() throws Exception {
        mockMvc.perform(
                        post("/account/sign-up")
                                .param("email", "ruby1@naver.com")
                                .param("nickname", "ruby1")
                                .param("password", "Ruby12!@")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated())
                .andExpect(view().name("redirect:/"));

        // ??????????????? ?????????????????? ??????
        Account account = accountRepository.findByEmail("ruby1@naver.com");
        assertThat(account).isNotNull();
        assertThat(account.getEmailCheckToken()).isNotNull();
        assertThat(account.getPassword()).isNotEqualTo("Ruby12!@");

        // ????????? ???????????? ??????
        then(emailService).should().sendEmail(any(EmailMessage.class));
    }

    @Test
    @DisplayName("???????????? ????????? ?????? - ????????? ??????")
    void checkedEmailWrongToken() throws Exception {
        mockMvc.perform(get("/account/check-email-token")
                .param("token", "fewagagagn")
                .param("email", "rubykim0723@gmail.com")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/checked-email"))
                .andExpect(model().attributeExists("error"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("???????????? ????????? ?????? - ????????? ??? ??????")
    void checkedEmailWrongEmail() throws Exception {
        Account account = accountRepository.findByEmail("rubykim0723@gmail.com");
        String token = account.getEmailCheckToken();

        mockMvc.perform(get("/account/check-email-token")
                        .param("token", token)
                        .param("email", "ruby3@naver.com")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/checked-email"))
                .andExpect(model().attributeExists("error"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("???????????? ????????? ?????? - ?????? ??????")
    void checkedEmail() throws Exception {
        Account account = accountRepository.findByEmail("rubykim0723@gmail.com");
        String token = account.getEmailCheckToken();

        mockMvc.perform(get("/account/check-email-token")
                        .param("token", token)
                        .param("email", "rubykim0723@gmail.com")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/checked-email"))
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("account"))
                .andExpect(authenticated());
    }

    @Test
    @DisplayName("????????? ?????? ?????? 50??? ??????")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateProfileOverLengthBio() throws Exception {
        mockMvc.perform(post("/account/profile")
                        .param("bio",
                                "??? ?????? ??????adsadgajsdhjasbdjashvfhjwvfadsahdjavfjhavfhjsavdhjasvdhjasdfngjk" +
                                        "desahvjhdahvfhjdsvfjhasvdhjavsdjhadiugquwiebdjhasd bhjbashdvavdavdhasdja")
                        .with(csrf()))
//                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("account/editProfile"));
    }


    @Test
    @DisplayName("????????? ??????")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateProfile() throws Exception {
        // ?????? DB??? ????????? account ??? ????????? ???????????? ?????????????????? @WithUserDetails ??? ?????? account ??? ?????????????????? ??????.
        Account account = accountRepository.findByEmail("rubykim0723@gmail.com");

        mockMvc.perform(post("/account/profile")
                        .param("bio", "??? ?????? ??????")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/account/" + account.getId()));
    }

    @Test
    @DisplayName("???????????? ????????? ????????? ??????????????? ?????????")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void passwordResetNotEquals() throws Exception {
        mockMvc.perform(post("/account/password-reset")
                        .param("password", "12!@qwQW")
                        .param("passwordConfirm", "12!@qwQWQW")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("account/password-reset"));
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ?????? ??????")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void passwordResetOverLength() throws Exception {
        mockMvc.perform(post("/account/password-reset")
                        .param("password", "12!@qwQWkbdghjasbhcsdvksdbfsdbfkjdsbwaceihfsadsadasdsadasdasddasd" +
                                "guakgcfacgkfkaeyugfuagcuwayvgfabfbadsfdasjdgadasdasasdsad")
                        .param("passwordConfirm", "12!@qwQWkbdghjasbhcsdvksdbfsdbfkjdsbwaceihfsadsadasdsadasdasddasd" +
                                "guakgcfacgkfkaeyugfuagcuwayvgfabfbadsfdasjdgadasdasasdsad")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("account/password-reset"));
    }

    @Test
    @DisplayName("???????????? ??????")
    @WithUserDetails(value = "rubykim0723@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void passwordReset() throws Exception {
        Account account = accountRepository.findByEmail("rubykim0723@gmail.com");

        mockMvc.perform(post("/account/password-reset")
                        .param("password", "12!@qwQWasAS")
                        .param("passwordConfirm", "12!@qwQWasAS")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/account/" + account.getId()));
    }
}