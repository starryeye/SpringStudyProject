package study.jwttutorial.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import study.jwttutorial.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // username 을 기준으로 User 엔티티를 가져올 때, 권한 정보도 같이 가져오도록 함.
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
