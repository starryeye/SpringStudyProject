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