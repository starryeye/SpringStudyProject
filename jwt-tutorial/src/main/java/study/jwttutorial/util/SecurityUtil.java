package study.jwttutorial.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * 간단한 유틸리티 메서드 모음
 */
public class SecurityUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    private SecurityUtil() {
    }

    /**
     * Security Context 의 Authentication 객체를 이용해 username 을 리턴해주는 역할
     *
     * 참고: Security Context 에 Authentication 객체가 저장되는 시점은, 개발 해놓은 JwtFilter 의 doFilter 메서드에서 요청이 들어올 때 이다.
     */
    public static Optional<String> getCurrentUsername() {

        //Security Context 에서 Authentication 객체를 꺼내온다.
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
            logger.debug("Security Context 에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        //꺼내온 Authentication 객체로 username 을 얻는다.
        String username = null;
        if(authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if(authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(username);
    }
}
