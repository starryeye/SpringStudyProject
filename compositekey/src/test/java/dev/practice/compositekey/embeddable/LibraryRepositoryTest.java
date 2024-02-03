package dev.practice.compositekey.embeddable;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class LibraryRepositoryTest {


    @Autowired
    private LibraryRepository libraryRepository;

    @DisplayName("복합키로 구성된 entity 를 다루어본다.")
    @Test
    void insert_and_select() {

        // given
        Library library = Library.create("1", "national central library", LocalDate.now());

        // when
        libraryRepository.save(library); // todo, insert 전.. DB 에 복합키 유무를 따지기 위해 select 가 먼저한번 나가버림..

        // then
        Library result = libraryRepository.findById(library.getId()).orElseThrow();

        assertThat(result.getId().getRegionNumber())
                .isEqualTo(library.getId().getRegionNumber());
        assertThat(result.getId().getName())
                .isEqualTo(library.getId().getName());
        assertThat(result.getEstablishmentDate())
                .isEqualTo(library.getEstablishmentDate());

        // library 와 result 는 서로 다른 영속성 컨텍스트에서 관리된 엔티티 이므로 인스턴스 다름
        // library 의 복합키 인스턴스와 result 복합키 인스턴스가 같은 이유는 (동일성) String 으로 이루어져 있어서 동등한 값이면 동일해서 인듯
        log.info("library = {}, library.getId() = {}", library, library.getId()); // getId() 를 출력하면 LibraryId 의 toString 출력 -> hashcode() 출력
        log.info("result = {}, result.getId() = {}", result, result.getId());
    }
}