package ruby.app.main;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ruby.app.account.service.AccountService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class MainControllerTest {

    @Autowired
    AccountService accountService;
    @Autowired
    MockMvc mockMvc;

    @BeforeAll
    void addAdmin() {
        accountService.signUp("ruby@naver.com", "ruby", "12!@qwQW");
    }

    @Test
    @DisplayName("존재하지 않는 계정으로 로그인 시도")
    void loginNotExistsAccount() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "enadweq@naver.com")
                        .param("password", "12!@qwQW")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시도")
    void loginWrongPassword() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "ruby@naver.com")
                        .param("password", "12!@qwQWasd")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "ruby@naver.com")
                        .param("password", "12!@qwQW")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("ruby@naver.com"));
    }
    
    @Test
    @DisplayName("로그아웃")
    @WithMockUser
    void logout() throws Exception {
        mockMvc.perform(post("/logout")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }

}