package dev.practice.multipledatasources.service.memo;

import dev.practice.multipledatasources.repository.memo.MemoEntity;
import dev.practice.multipledatasources.repository.memo.MemoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemoServiceTest {

    @Autowired
    private MemoService memoService;

    @Autowired
    private MemoRepository memoRepository;

    @AfterEach
    void tearDown() {
        memoRepository.deleteAllInBatch();
    }

    @DisplayName("memoTransactionManager 로 적용이 잘 되면, 변경 감지가 정상 동작한다.")
    @Test
    void test() {

        // given
        MemoEntity given = MemoEntity.builder()
                .title("memo title")
                .content("before change")
                .build();
        memoRepository.save(given);

        // when
        memoService.editContent(given.getId(), "after change");

        // then
        MemoEntity result = memoRepository.findById(given.getId()).orElseThrow();
        assertThat(result.getId()).isEqualTo(given.getId());
        assertThat(result.getTitle()).isEqualTo(given.getTitle());
        assertThat(result.getContent()).isEqualTo("after change");
        assertThat(result.getCreatedAt()).isEqualTo(given.getCreatedAt());
        assertThat(result.getLastModifiedAt()).isNotEqualTo(given.getLastModifiedAt());
    }

    @DisplayName("todoTransactionManager 로 잘못 적용 하면, 변경 감지가 정상 동작 하지 않는다.")
    @Test
    void test2() {

        // given
        MemoEntity given = MemoEntity.builder()
                .title("memo title")
                .content("before change")
                .build();
        memoRepository.save(given);

        // when
        /**
         * editContentWithWrongTransaction 에는 memoTransactionManager 가 아니라
         * todoTransactionManager 로 잘못 적용 되어있지만,
         * findById 는 정상 동작한다. (변경 감지는 동작하지 않음)
         * -> 이유는 WrongTransactionManagerTest 로 가서 보자
         */
        memoService.editContentWithWrongTransaction(given.getId(), "after change");

        // then
        MemoEntity result = memoRepository.findById(given.getId()).orElseThrow();
        assertThat(result.getId()).isEqualTo(given.getId());
        assertThat(result.getTitle()).isEqualTo(given.getTitle());
        assertThat(result.getContent()).isEqualTo("before change");
        assertThat(result.getCreatedAt()).isEqualTo(given.getCreatedAt());
        assertThat(result.getLastModifiedAt()).isEqualTo(given.getLastModifiedAt());
    }

}