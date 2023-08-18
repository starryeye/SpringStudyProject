package dev.practice.nplusone.lazy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("ManyToOne 관계에서 findAll 을 하여 조회 하면 조회 된 만큼 추가 쿼리가 나갈 수 있다.")
    @Test
    @Transactional
    void findAll_And_N_Plus_One_Problem() {

        // given
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        // when
        System.out.println("===============when 쿼리===============");
        List<Member> result = memberRepository.findAll();
        System.out.println("===============when 쿼리===============");

        // then
        result.forEach(
                member -> assertThat(persistenceUnitUtil.isLoaded(member.getTeam())).isFalse()
        );


        System.out.println("===============Lazy Loading 으로 인한 추가 쿼리===============");

        Set<Team> teams = new HashSet<>();

        result.forEach(
                member -> {
                    member.getTeam().getName(); // 프록시 초기화
                    teams.add(member.getTeam());
                });

        System.out.println("===============Lazy Loading 으로 인한 추가 쿼리===============");

        System.out.println("result.size() : " + result.size() + ", N : " + teams.size());
    }
}