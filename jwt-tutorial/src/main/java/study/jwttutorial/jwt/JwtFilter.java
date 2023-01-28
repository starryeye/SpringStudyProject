package study.jwttutorial.jwt;

import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * JWT 를 위한 커스텀 필터
 * GenericFilterBean 을 상속 받아서 doFilter Override, 실제 필터링 로직은 doFilter 내부에 작성
 */
public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * JWT 토큰의 인증 정보를 현재 실행 중인 SecurityContext 에 저장하는 역할 수행
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }
}
