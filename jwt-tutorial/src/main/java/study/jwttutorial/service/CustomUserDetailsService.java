package study.jwttutorial.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.jwttutorial.entity.User;
import study.jwttutorial.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security 에서 중요한.. UserDetailsService 를 구현한 클래스
 */

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * UserDetailsService 를 구현하기 위해 loadUserByUsername 메서드 오버라이딩
     *
     * 로그인 시, DB 에서 User 엔티티와 Authority 엔티티를 가져와서 해당 정보를 기반으로
     * userDetails.User 객체를 생성해서 리턴
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneWithAuthoritiesByUsername(username)
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터 베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(String username, User user) {

        // 유저 엔티티 활성화 여부 체크
        if(!user.isActivated()) {
            throw new RuntimeException(username + " -> 활성화 되어 있지 않습니다.");
        }

        // 유저 엔티티가 활성화 되어 있다면 유저 정보에서 권한 정보 이름들을 기반으로 SimpleGrantedAuthority 객체를 생성해 리스트로 받는다.
        List<GrantedAuthority> grantedAuthorityList = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        // 받아온 권한 정보 리스트와 유저 엔티티 정보를 기반으로 유저 객체를 생성해 리턴
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                grantedAuthorityList
        );
    }
}
