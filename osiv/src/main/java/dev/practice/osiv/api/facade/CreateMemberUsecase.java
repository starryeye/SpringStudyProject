package dev.practice.osiv.api.facade;

import dev.practice.osiv.api.service.MemberService;
import dev.practice.osiv.api.service.NoticeService;
import dev.practice.osiv.domain.Member;
import dev.practice.osiv.domain.Notice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateMemberUsecase {

    private final MemberService memberService;
    private final NoticeService noticeService;

    public void run(String name) {

        /**
         *
         * memberService 와 noticeService 는
         * 동일한 영속성 컨텍스트를 공유하지만
         * 서로 다른 트랜잭션이다.
         */

        // spring OSIV 로 인해 member 는 영속 상태이다.
        // -> 인터셉터에서 영속성 컨텍스트를 생성하고 인터셉터 범위를 벗어날때까지 하나의 영속성 컨텍스트를 공유함
        Member member = memberService.create(name);

        // 영속 상태의 엔티티를 변경했지만, 스프링 OSIV 는 트랜잭션(memberService.create)이 끝나면 더이상 flush 하지 않아서 DB 에 반영되지는 않는다.
        // -> 트랜잭션(memberService.create) 이 커밋 되는 시점에 flush 가 동작하며 변경감지가 이미 동작했고, interceptor 에서 영속성 컨텍스트가 종료될때는 flush 동작이 없다.
        // 사용자가 직접 flush 하면 트랜잭션 외부에서 수정이므로 TransactionRequiredException 이 발생한다. (트랜잭션 외부에서 조회는 가능하다. - 지연 로딩도 가능)
        member.changeName("change name");
        String changed = member.getName();
        String message = Notice.generateCreateMemberMessage(changed);

        log.info("diff transaction");

        /**
         * 아래 noticeService.create() 에서는 memberService.create() 와 다른 transaction 이 시작되고 종료된다.
         * 하지만, 영속성 컨텍스트는 동일하다. (DB connection 도 아마 동일..)
         *
         * 그래서, 전혀 다른 transaction 이 동작하지만,
         * 영속성 컨텍스트에는 1차 캐시에 member 의 스냅샷과 엔티티가 저장되어 있다.
         * 해당 transaction 이 종료되면서..
         * 변경 감지가 동작해버려.. member 의 변경 사항이 DB 에 전달된다.
         */
        noticeService.create(message);
    }

    /**
     * 위와 같은 현상을 간단하게 해결하려면
     * 스프링 OSIV 를 적용하지 않고.. 트랜잭션 범위와 영속성 컨텍스트 범위를 동일하게 맞추면 된다.
     * spring.jpa.open-in-view: false 로 하자..
     *
     * 그러면, memberService.create() 에서 반환된 member 는 준영속 상태가 된다.
     *
     * 대신, 트랜잭션 범위(=영속성 컨텍스트 범위) 를 벗어난 상황에서 지연 로딩을 시도하면,
     * LazyInitializationException 이 발생한다.
     *
     * OSIV 와 성능
     * https://www.baeldung.com/spring-open-session-in-view
     */
}
