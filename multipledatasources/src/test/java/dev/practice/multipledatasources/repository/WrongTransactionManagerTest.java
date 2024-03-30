package dev.practice.multipledatasources.repository;

import dev.practice.multipledatasources.repository.memo.MemoEntity;
import dev.practice.multipledatasources.repository.memo.MemoRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class WrongTransactionManagerTest {

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private EntityManager memoEntityManager;

    @DisplayName("@Transactional 없이는 동일성 보장이 안된다.")
    @Test
    void test() {

        // given
        MemoEntity given = MemoEntity.builder()
                .title("memo title")
                .content("memo content")
                .build();
        memoRepository.save(given);

        // when
        /**
         * findById 특징
         * - findById 는 기본적으로 @Transactional 이 적용 되어있지 않다.
         *  - @Transactional 없어도 동작한다는 말..
         *      - findById 를 통해 조회된 엔티티도 영속성 컨텍스트의 관리를 받지만, 이 경우에는 메서드 실행이 완료되자마자 준영속이 된다.
         *  - findById 는 사용자 코드에서 @Transactional 이 걸려있어야 동일성 보장이 된다..
         *      - 즉, 준영속 상태이므로 변경감지, 동일성 보장, 지연 로딩, 쓰기 지연 등등.. 영속성 컨텍스트의 지원을 받지 못하는
         */
        // DB에 쿼리가 2회 전달됨
        MemoEntity result1 = memoRepository.findById(given.getId()).orElseThrow();
        MemoEntity result2 = memoRepository.findById(given.getId()).orElseThrow();

        // then
        log.info("result1 = {}", result1.hashCode());
        log.info("result2 = {}", result2.hashCode());
        assertThat(result1).isNotEqualTo(result2);

        assertThat(memoEntityManager.contains(result1)).isFalse(); // 두 엔티티 모두 준영속 상태이다.
        assertThat(memoEntityManager.contains(result2)).isFalse();
    }

    @Transactional(transactionManager = "todoTransactionManager") // memo 가 아닌 todo 로 잘못 적용함
    @DisplayName("@Transactional 이 잘못 걸려있는 상황이면, @Transactional 이 없는 것처럼 된다.")
    @Test
    void test2() {

        // given
        MemoEntity given = MemoEntity.builder()
                .title("memo title")
                .content("memo content")
                .build();
        memoRepository.save(given);

        // when
        // DB에 쿼리가 2회 전달됨
        MemoEntity result1 = memoRepository.findById(given.getId()).orElseThrow();
        MemoEntity result2 = memoRepository.findById(given.getId()).orElseThrow();

        // then
        log.info("result1 = {}", result1.hashCode());
        log.info("result2 = {}", result2.hashCode());
        assertThat(result1).isNotEqualTo(result2);

        assertThat(memoEntityManager.contains(result1)).isFalse(); // 두 엔티티 모두 준영속 상태이다.
        assertThat(memoEntityManager.contains(result2)).isFalse();
    }
}
