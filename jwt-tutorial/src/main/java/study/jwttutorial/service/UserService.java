package study.jwttutorial.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.jwttutorial.dto.UserDto;
import study.jwttutorial.entity.Authority;
import study.jwttutorial.entity.User;
import study.jwttutorial.exception.DuplicateMemberException;
import study.jwttutorial.exception.NotFoundMemberException;
import study.jwttutorial.repository.UserRepository;
import study.jwttutorial.util.SecurityUtil;

import java.util.Collections;
import java.util.Optional;

/**
 * 회원가입, 유저 정보 조회 등의 메서드를 만들기 위한 UserService 클래스
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입 로직의 메서드
     */
    @Transactional
    public User signup(UserDto userDto) {

        //username 이 DB 에 존재하는지 확인
        if(userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        //username 이 DB 에 존재 하지 않으면, 권한 정보 생성
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER") // ROLE_USER 라는 권한을 줬다.
                .build();

        // 만들어진 권한정보 와 파라미터로 들어온 UserDto 를 기반으로 User 엔티티를 생성
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        // user 엔티티를 DB 에 저장하고 저장한 user 엔티티를 리턴
        return userRepository.save(user);
    }

    /**
     * username 을 기반으로 DB 에서, user 엔티티와 authority 엔티티를 조인해서 가져옴
     */
    @Transactional(readOnly = true)
    public User getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username)
                .orElseThrow(() -> new NotFoundMemberException("회원을 찾을 수 없습니다."));
    }

    /**
     * 현재 Security Context 에 저장되어 있는 username 에 해당하는..
     * user 엔티티와 authority 엔티티를 조인하여 user 엔티티를 가져온다.
     */
    @Transactional(readOnly = true)
    public User getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new NotFoundMemberException("회원을 찾을 수 없습니다."));
    }
}
