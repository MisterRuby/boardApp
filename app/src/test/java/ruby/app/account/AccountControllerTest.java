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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ruby.app.account.repository.AccountRepository;
import ruby.app.account.service.AccountService;
import ruby.app.domain.Account;

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
    JavaMailSender mailSender;


    @BeforeAll
    void addAdmin() {
        accountService.signUp("admin@naver.com", "admin", "123!@#qweQWE");
        accountService.signUp("ruby@naver.com", "ruby", "123!@#qweQWE");
        Mockito.reset(mailSender);
    }

    @Test
    @DisplayName("index 테스트")
    void index() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("회원가입 화면 테스트")
    void signUpForm() throws Exception {
        mockMvc.perform(get("/account/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @Test
    @DisplayName("회원가입 처리 - 이메일 오류")
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
    @DisplayName("회원가입 처리 - 닉네임 오류")
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
    @DisplayName("회원가입 처리 - 비밀번호 오류")
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
    @DisplayName("회원가입 처리 - 입력값 전체 오류")
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
    @DisplayName("회원가입 처리 - 이메일 중복 오류")
    void validateExistsEmail() throws Exception {
        mockMvc.perform(
                        post("/account/sign-up")
                                .param("email", "ruby@naver.com")
                                .param("nickname", "ruby1")
                                .param("password", "adaASD12$$")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeHasFieldErrorCode("signUpForm", "email", "invalid.email"));
    }

    @Test
    @DisplayName("회원가입 처리 - 닉네임 중복 오류")
    void validateExistsNickname() throws Exception {
        mockMvc.perform(
                        post("/account/sign-up")
                                .param("email", "ruby1@naver.com")
                                .param("nickname", "ruby")
                                .param("password", "adaASD12$$")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeHasFieldErrorCode("signUpForm", "nickname", "invalid.nickname"));
    }

    @Test
    @DisplayName("회원가입 처리 - 입력값 정상 입력")
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

        // 회원정보가 저장되었는지 확인
        Account account = accountRepository.findByEmail("ruby1@naver.com");
        assertThat(account).isNotNull();
        assertThat(account.getEmailCheckToken()).isNotNull();
        assertThat(account.getPassword()).isNotEqualTo("Ruby12!@");

        // 메일을 보냈는지 확인
        then(mailSender).should().send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("회원가입 이메일 인증 - 토큰값 오류")
    void checkedEmailWrongToken() throws Exception {
        mockMvc.perform(get("/account/check-email-token")
                .param("token", "fewagagagn")
                .param("email", "ruby@naver.com")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/checked-email"))
                .andExpect(model().attributeExists("error"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("회원가입 이메일 인증 - 이메일 값 오류")
    void checkedEmailWrongEmail() throws Exception {
        Account account = accountRepository.findByEmail("ruby@naver.com");
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
    @DisplayName("회원가입 이메일 인증 - 정상 인증")
    void checkedEmail() throws Exception {
        Account account = accountRepository.findByEmail("ruby@naver.com");
        String token = account.getEmailCheckToken();

        mockMvc.perform(get("/account/check-email-token")
                        .param("token", token)
                        .param("email", "ruby@naver.com")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/checked-email"))
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(authenticated());
    }



    @Test
    @DisplayName("로그인 화면 테스트")
    void loginForm() {

    }

    @Test
    @DisplayName("비밀번호 변경 화면 테스트")
    void passwordResetForm() {

    }

}