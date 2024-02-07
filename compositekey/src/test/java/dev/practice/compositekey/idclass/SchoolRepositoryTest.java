package dev.practice.compositekey.idclass;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class SchoolRepositoryTest {

    @Autowired
    private SchoolRepository schoolRepository;

    @DisplayName("IdClass 복합키로 구성된 entity 를 다루어본다.")
    @Test
    void insert_and_select() {

        // given
        SchoolId id = SchoolId.builder()
                .regionNumber("2")
                .name("Daegu elementary school")
                .build();
        School school = School.create(id.getRegionNumber(), id.getName(), LocalDate.now());

        // when
        log.info("----------------------before when----------------------");
        schoolRepository.save(school); // todo, insert 전.. DB 에 복합키 유무를 따지기 위해 select 가 먼저한번 나가버림..
        // insert_and_select 에 @Transactional 을 걸면, 쓰기 지연이 적용되나.. 복합키 유무 select 는 여전히 before, after when 사이에 이루어진다.
        log.info("----------------------after when----------------------");

        // then
        log.info("----------------------before then----------------------");
        School result = schoolRepository.findById(id).orElseThrow();
        log.info("----------------------after then----------------------");

        assertThat(result.getRegionNumber()).isEqualTo(school.getRegionNumber());
        assertThat(result.getName()).isEqualTo(school.getName());
        assertThat(result.getEstablishmentDate()).isEqualTo(school.getEstablishmentDate());
    }

}