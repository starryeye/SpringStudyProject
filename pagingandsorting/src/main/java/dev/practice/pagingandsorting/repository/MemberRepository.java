package dev.practice.pagingandsorting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Paging (Pagination) 과 Sorting
     *
     * 쿼리 메서드의 파라미터로 아래를 사용할 수 있다.
     * org.springframework.data.domain.Sort : 정렬 기능
     * org.springframework.data.domain.Pageable : 페이징 기능 (내부에 Sort 포함)
     *
     * 쿼리 메서드의 파라미터로 Pageable 을 사용하면, 리턴 타입으로 아래를 사용할 수 있다.
     * org.springframework.data.domain.Page
     * Page 가 리턴 타입이면 기본적으로 전체 count 쿼리를 추가로 호출한다.
     */

    List<Member> findByName(String name, Sort sort);
}
