package dev.practice.pagingandsorting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Paging (Pagination) 과 Sorting
     *
     * 쿼리 메서드의 파라미터로 아래를 사용할 수 있다.
     * - org.springframework.data.domain.Sort : 정렬 기능
     * - org.springframework.data.domain.Pageable : 페이징 기능 (내부에 Sort 포함)
     *
     * 쿼리 메서드의 파라미터로 Pageable 을 사용하면(?), 리턴 타입으로 아래를 사용할 수 있다.
     * - org.springframework.data.domain.Page
     * Page 가 리턴 타입이면 기본적으로 전체 count 쿼리를 추가로 호출한다.
     * - org.springframework.data.domain.Slice
     * Page 는 Slice 를 상속한다. Slice 는 전체 count 쿼리를 추가로 호출하지 않아서 전체 갯수나 전체 페이지에 대한 정보를 제공할 순 없다.
     *
     * todo, JPQL + Page or Slice
     */

    List<Member> findByName(String name, Sort sort); // MemberRepositoryTest::sort()

    List<Member> findListByName(String name, Pageable pageable); // MemberRepositoryTest::pageable()

    Page<Member> findPageByName(String name, Pageable pageable); // MemberRepositoryTest::page()
}
