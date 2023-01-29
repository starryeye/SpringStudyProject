package study.jwttutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.jwttutorial.jwt.JwtAccessDeniedHandler;
import study.jwttutorial.jwt.JwtAuthenticationEntryPoint;
import study.jwttutorial.jwt.JwtSecurityConfig;
import study.jwttutorial.jwt.TokenProvider;

/**
 * @EnableWebSecurity : 기본적인 웹 보안을 활성화 하겠다는 의미
 * -> 추가적인 설정을 위해서, WebSecurityConfigurer 를 implements 하거나 WebSecurityConfigurerAdapter 를 extends 하는 방법이 있다.
 * -> WebSecurityConfigurerAdapter 가 deprecated 되었다...
 *
 * @EnableGlobalMethodSecurity : @PreAuthorize 어노테이션을 메서드 단위로 사용하기 위해 적용한 어노테이션
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // jwt 패키지에 각각 ComponentScan 을 통해 등록한 빈들을 주입 받는다.
    public SecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    /**
     * PasswordEncoder 는 BCryptPasswordEncoder 를 사용
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //h2-console 하위 모든 요청과 파비콘 관련 요청은 Spring Security 로직을 수행하지 않기 위함.
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        "/h2-console/**",
                        "/favicon.ico"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 토큰 방식을 사용하기 때문에 csrf 설정은 disable 시킨다.
                .csrf().disable()

                // Exception 을 핸들링 할때 사용 할 AuthenticationEntryPoint 와 AccessDeniedHandler 를 jwt 패키지에서 만든 클래스로 적용하도록 함
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                //h2-console 을 위한 설정 추가
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정 한다.
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests() //HttpServletRequest를 사용하는 요청들에 대한 접근 제한을 설정하겠다.
                .antMatchers("/api/hello").permitAll() // /api/hello 에 대한 요청은 인증없이 접근을 허용하겠다.
                .antMatchers("/api/authenticate").permitAll() // /api/authenticate (로그인) 에 대한 요청은 인증없이 접근을 허용하겠다.
                .antMatchers("/api/signup").permitAll() // /api/signup (회원가입) 에 대한 요청은 인증없이 접근을 허용하겠다.
                .anyRequest().authenticated() //나머지 요청들은 모두 인증을 받겠다.

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

    }
}
