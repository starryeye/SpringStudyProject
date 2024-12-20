package study.jwttutorial.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 유효한 자격 증명을 제공하지 않고 접근하려 할 때, 401 Unauthorized 에러를 리턴하기 위해서
 * AuthenticationEntryPoint 를 implements 한 클래스
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // implements 하기 위한 오버라이딩 메서드
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        //401 Unauthorized 에러 send
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
