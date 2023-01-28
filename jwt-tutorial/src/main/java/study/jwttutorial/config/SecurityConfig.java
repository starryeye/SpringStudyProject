package study.jwttutorial.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @EnableWebSecurity : 기본적인 웹 보안을 활성화 하겠다는 의미
 * -> 추가적인 설정을 위해서, WebSecurityConfigurer 를 implements 하거나 WebSecurityConfigurerAdapter 를 extends 하는 방법이 있다.
 * -> WebSecurityConfigurerAdapter 가 deprecated 되었다...
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
                .authorizeRequests() //HttpServletRequest를 사용하는 요청들에 대한 접근 제한을 설정하겠다.
                .antMatchers("/api/hello").permitAll() // /api/hello 에 대한 요청은 인증없이 접근을 허용하겠다.
                .anyRequest().authenticated(); //나머지 요청들은 모두 인증을 받겠다.
    }
}
