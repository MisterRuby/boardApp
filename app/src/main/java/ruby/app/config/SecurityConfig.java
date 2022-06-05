package ruby.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import ruby.app.account.service.AccountService;

import javax.sql.DataSource;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final DataSource dataSource;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * 시큐리티 인증 제외 url 설정
     * @return
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /** 페이지 권한 설정 */
        http
            .authorizeRequests()
            .mvcMatchers("/boards/add").authenticated()            // /boards/{boardId} 에서 허용되어버리므로 별도로 막아줌
            .mvcMatchers("/", "/account/sign-up",
                 "/account/check-email-token", "/account/password-reset",
                 "/boards", "/boards/{boardId}").permitAll()
            .anyRequest().authenticated();

        /** 로그인 처리 */
        http.formLogin()                               // 스프링 시큐리티가 제공하는 기본 로그인 화면 사용
                .loginPage("/login").permitAll();      // 가본 로그인 화면 x. 로그인 화면처리 핸들러 지정

        /** 로그아웃 처리 */
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");                 // 로그아웃 처리 후 이동 페이지

        http.rememberMe()
                .userDetailsService(userDetailsService)
                .tokenRepository(tokenRepository());

        return http.build();
    }

    /**
     * PasswordEncoder Bean 등록
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * RememberMe 에 사용되는 repository 를 빈으로 등록
     *  - db에 접근하고 토큰 관련 정보를 저장하고 사용하기 위해 repository 가 대상으로 하는 테이블을 생성해줘야 한다.
     *      - jpa 의 경우 엔티티 필요
     * @return
     */
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
