package study.jwttutorial.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * 토큰의 생성, 토큰의 유효성 검증등을 담당하는 클래스
 */
@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    /**
     * TokenProvider 빈이 생성이 되고 의존성 주입 받은 후,
     * secret 값을 Base64 Decode 해서 key 변수에 할당하기 위해
     * InitializingBean 을 상속 받아서 afterPropertiesSet 을 오버라이딩 함.
     */
    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * authentication 객체에 포함되어 있는 권한 정보들을 이용하여
     * 토큰을 생성하는 createToken 메서드
     */
    public String createToken(Authentication authentication) {

        //Spring Security Authentication 의 권한들 설정
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //만료 시간 설정
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        //JWT 토큰 생성
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * JWT 토큰을 파라미터로 받아서 Token 에 담겨있는 정보를 이용해 Authentication 객체를 리턴하는 메서드
     */
    public Authentication getAuthentication(String token) {

        //토큰으로 클레임 생성
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        //클레임에서 권한 정보들을 빼냄
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //권한 정보를 이용해 유저 객체 생성
        User principal = new User(claims.getSubject(), "", authorities);

        //유저 객체와 토큰, 권한 정보들로 Authentication 객체를 생성 후 리턴
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 토큰을 파라미터로 받아서 토큰의 유효성을 검증하는 메서드
     */
    public boolean validateToken(String token) {

        //토큰을 파싱해서 나오는 예외를 캐치함
        try {
            //토큰 파싱 해봄.
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
