package study.jwttutorial.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import study.jwttutorial.dto.LoginDto;
import study.jwttutorial.dto.TokenDto;
import study.jwttutorial.jwt.JwtFilter;
import study.jwttutorial.jwt.TokenProvider;

import javax.validation.Valid;

/**
 * 로그인 API Controller
 */

@RestController
@RequestMapping("/api")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        //요청 데이터의 username, password 정보로 UsernamePasswordAuthenticationToken 객체를 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        /*
          생성한 authenticationToken 객체를 넘기면서 AuthenticationManagerBuilder 의 authenticate 메서드가 실행 될 때..
          UserDetailsService 를 구현 한 CustomUserDetailsService 의 loadUserByUsername 메서드가 실행 된다.
          수행 후, Authentication 객체 리턴 받음
         */
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 리턴 받은 Authentication 객체를 SecurityContext 에 저장한다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 리턴 받은 Authentication 객체로 JWT 토큰을 생성한다.
        String jwt = tokenProvider.createToken(authentication);

        //생성한 JWT 토큰을 Http header 에 넣어 준다.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        //생성한 JWT 토큰을 응답 바디에도 넣어 준다.
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
