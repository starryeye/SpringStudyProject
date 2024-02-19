package dev.practice.fetchjoin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 페치 조인..
     * 연관된 엔티티나 컬렉션 엔티티도 함께 조회 하는 기능이다.
     *
     * 특징과 한계
     * 1. 글로벌 로딩 전략 보다 JPQL 이 우선 된다.
     * -> 글로벌 로딩 전략을 Lazy 로 하고 join fetch 를 하면 join fetch 가 우선이다.
     * -> 글로벌 로딩 전략을 eager 로 하고 join 을 하면 join 이 우선으로 동작하고 그 이후에 바로 한번더 쿼리가 나간다.
     *
     * 2. 글로벌 로딩 전략을 Lazy 로 설정하고 필요한 경우에 join fetch 를 사용하자.
     * -> Eager 로 하면, 사용하지 않는 엔티티도 함께 로딩하게 되는 경우가 발생하므로 성능상 좋지 못하다.
     *
     *
     * 3. JPQL 에서 일반 join 에서는 "join m.team t" 와 같이 별칭을 지원하지만,
     * join fetch 에서는 "join fetch m.team t" 와 같이 별칭을 사용하지 못한다.
     *
     * 4. 둘 이상의 컬렉션 엔티티를 페치 조인 할 수 없다. (하나의 엔티티에 여러개의 컬렉션 엔티티가 존재하는데 그 두개를 모두 페치 조인하는 경우인듯)
     * -> 구현체에 따라 되기도 하는데 hibernate 에서는 MultipleBagFetchException 이 발생한다.
     * -> 둘 이상의 컬렉션 엔티티를 fetch 하면 경우의 수가.. 컬렉션 수 * 컬렉션 수 이므로 카테시안 곱이 만들어진다.
     *
     * 5. 컬렉션 엔티티를 페치 조인하면 페이징 API (setFirstResult, setMaxResults) 를 사용할 수 없다.
     * -> 컬렉션 엔티티가 아닌 단일 엔티티(OneToOne, ManyToOne) 연관 관계에서는 사용할 수 있다.
     * -> hibernate 에서는 컬렉션 엔티티를 페치 조인하고 페이징 API 를 사용하면, 메모리에서 페이징 처리를 하여 성능 문제가 생길 수 있으므로 사용하면 안된다.
     * -> 배치 사이즈로 해결하도록 해보자.
     *
     * 6. 일반 조인과의 차이점..
     * -> 일반 조인으로 조회하면 연관관계에 있는 엔티티는 결과로 받아 오지 않고 root 엔티티만 결과로 받아온다.
     * -> 연관관계에 있는 엔티티들까지 함께 영속화 하여 받아오려면 꼭 페치 조인을 사용해야한다.
     *
     */

    /**
     * 엔티티 페치 조인
     */
    @Query(
            value = """
                    select m
                    from Member m
                    join fetch m.team
                    """
    )
    List<Member> findAllBy();
}
