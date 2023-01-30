package study.jwttutorial.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import study.jwttutorial.dto.UserDto;
import study.jwttutorial.entity.User;
import study.jwttutorial.service.UserService;

import javax.validation.Valid;

/**
 * UserService 의 메서드를 호출하는 Controller
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원 가입 api
     * UserDto 를 받아서 UserService 의 signup 메서드 호출
     */
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(new UserDto(userService.signup(userDto)));
    }

    /**
     * @PreAuthrize 어노테이션을 통해서..
     * 요청 헤더로 들어온 토큰이 ADMIN 또는 USER text 가 있는 권한이라면
     * user 엔티티를 응답
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDto> getMyUserInfo() {
        return ResponseEntity.ok(new UserDto(userService.getMyUserWithAuthorities().get()));
    }

    /**
     * @PreAuthorize 어노테이션을 통해서..
     * 요청 헤더로 들어온 토큰이 ADMIN text 가 있는 권한이라면
     * 경로 변수로 들어온 username 을 가진 user 엔티티를 응답
     */
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(new UserDto(userService.getUserWithAuthorities(username).get()));
    }
}
