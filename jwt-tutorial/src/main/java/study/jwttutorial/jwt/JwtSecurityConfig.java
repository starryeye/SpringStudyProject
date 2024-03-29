package study.jwttutorial.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * TokenProvider, JwtFilter 를 SecurityConfig 에 적용하기 위한 클래스
 */
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;

    /**
     * SecurityConfigurerAdapter 를 extends 하고 Configure 메서드를 오버라이딩
     * TokenProvider 를 주입받아서
     * JwtFilter 를 생성한다.
     * Security 로직에 생성한 JwtFilter 를 등록하는 메서드
     */
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
