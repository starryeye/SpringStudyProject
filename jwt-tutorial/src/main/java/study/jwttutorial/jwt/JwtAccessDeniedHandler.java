package study.jwttutorial.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 필요한 권한이 존재하지 않는 경우에 403 Forbidden 에러를 리턴하기 위해서
 * AccessDeniedHandler 를 implements 한 클래스
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    // implements 하기 위한 오버라이딩 메서드
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        //403 Forbidden 에러 send
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
