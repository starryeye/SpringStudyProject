package dev.practice.coupling.phase4_event.service;

import dev.practice.coupling.dto.RegisterMemberRequest;
import dev.practice.coupling.dto.RegisterMemberResponse;
import dev.practice.coupling.phase4_event.event.RegisteredMemberEvent;
import dev.practice.coupling.repository.Member;
import dev.practice.coupling.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceV4 {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public RegisterMemberResponse registerProcess(RegisterMemberRequest request) {

        // 회원 등록
        Member member = Member.create(request.name(), request.email());
        Member registered = memberRepository.save(member);

        // 회원 등록 이벤트 발행
        RegisteredMemberEvent event = RegisteredMemberEvent.create(registered.getId(), registered.getName(), registered.getEmail());
        eventPublisher.publishEvent(event);

        return RegisterMemberResponse.of(registered);
    }

    /**
     * phase 1 의 문제들을 event 개념을 도입함으로써 어떤게 해결되었는지 알아보자.
     *
     * 1. 기능간 강결합
     * 해결 됨
     * EmailService 가 별도의 모듈이나 별도의 시스템으로 분리되어야 할 경우 MemberService 의 코드를 변경하지 않아도 된다.
     * -> MemberService 의 이벤트 발행 구현 코드는 변경될 수는 있겠지만, 내용은 변하지 않는다.
     *
     * 2. transaction 경계
     * 해결 됨
     * @Async 로 서로 다른 스레드로 동작하도록 만들었기 때문에 트랜잭션이 분리되었다.
     * 메인 로직인 회원 가입은 성공하고 메일 보내기 기능이 실패해도 메인 로직이 롤백되지는 않는다.
     *
     * 3. 동기적인 방식
     * 해결됨
     * @TransactionalEventListener 을 사용하여도.. 기본적으로 동기 블로킹.. MemberService 로직에서 EmailService 를 호출하지만,
     * @Async 로 인해 서로 다른 스레드로 동작하며 (non-blocking)
     * 코드상 MemberService 은 EmailService 완료 시점 및 결과에 아무런 관심이 없어졌으므로 비동기이다.
     * -> EmailService 처리 시간은 API 응답 시간에 영향을 주지 않게 되었다.
     *
     * 4. commit 시점과 메일 발송 시점
     * 해결됨
     * @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) 을 사용하여
     * MemberService 의 트랜잭션 commit 이후 EmailService 트랜잭션이 시작되기 때문에
     * MemberService 트랜잭션이 롤백 되었는데 EmailService 가 수행되지는 않을 것이다.
     *
     */

    /**
     * 이벤트와 결합
     *
     * 1. 코드간의 강한 결합을 시스템 분리로 느슨하게 만들 수 있나요?
     * - 코드간의 결합이 일어나면 도메인을 다른 시스템으로 분리할 때 문제가 발생할 수 있다.
     * - 물리적인 시스템 분리를 하게 되면, 기존 코드레벨 호출이 http 통신 요청으로 변경된다.
     *   하지만 http 통신 요청을 해야하는 의도가 남아있기 때문에 결합이 느슨해졌다고 할 수 없다.
     *
     *
     * 2. 비동기 방식에서도 강한 결합 발생할 수 있다.
     * - @Async를 사용해서 직접적인 의존을 제거할 수 있다.
     *   하지만, 스레드의 의존을 제거하는 것이지.. 의도가 담겨있는 호출을 하면 강한 결합이다.
     *
     * ex. 1, 2 번에 대한 예제
     * 회원가입() { // 회원등록과 감사이메일발송의 강한 결합
     *     등록()
     *     감사이메일발송()
     * }
     * -> 감사이메일발송 내부 로직에서 이메일 발송 서버로 http 요청을 하던, 회원 서버 자체적으로 이메일 발송을 하던.. 비동기로 처리하던..
     *    회원등록과 감사이메일발송 간 강한 결합인 것은 변하지 않는다.
     *
     *
     * 3. 이벤트 방식에서의 강한 결합
     * - 메시징 시스템을 사용하면, 물리적인 의존은 제거할 수 있지만 완벽한 느슨한 결합과 이벤트 아키텍처가 아닐 수 있다.
     *   발행한 메시지가 대상 도메인에게 기대하는 목적이 있으면 비동기 로직일 뿐이다. (2 번과 동일)
     *      - eventPublisher.sendSuccessMail(memberId)
     *        회원가입 → 이메일 발송 이벤트 발행 (호출 의도가 담겨있음, 회원가입이 이메일 발송을 알게된 것)
     * - 회원가입 로직을 마쳤을 때, 회원가입이 완료되었다는 이벤트를 발행해야한다.
     *   메일 서비스는 회원가입 완료 이벤트를 구독하여 해당 이벤트가 발생했을 때 실행한다.
     *   즉, 회원가입 로직은 메일 서비스에 관여 하지 않은 것이다. 이로인해 두 시스템의 의존성이 느슨해질 수 있다.
     *      - eventPublisher.registeredMember(memberId)
     *        회원가입 → 회원가입이 되었다는 이벤트 발행 (의도가 없음, 회원가입이 이메일 발송을 모름)
     *
     * 결론
     * "우리가 발행해야할 이벤트는 이벤트로 인해 달성하려는 목적이 아닌 도메인 이벤트 그 자체이다."
     * -> 우리는 회원가입 감사 메일 발송 이벤트를 발행하는 것이 아니라
     *    회원가입이 되었다는 도메인의 상태 변경에 대한 이벤트를 발행하는 로직을 구현해야한다.
     */

    /**
     * [이벤트 장단점]
     * 장점
     *     의존성을 분리하여 두 클래스를 느슨하게 결합시킬 수 있음
     *     클래스가 독립적이므로 재사용성을 높일 수 있음
     *     추후에 별도의 서비스로 분리하기 용이함
     *     메세지 구독 모듈을 추가 또는 삭제할 때, 다른 모듈에 영향을 주지 않은 채로 수정할 수 있음
     *     단위 테스트가 용이해짐
     *
     * 단점
     *     코드 흐름을 따라가기 어려움
     *     메세지 구독 순서를 고려해야 하는 경우 복잡해짐
     *     전체적인 이벤트의 구독 및 발행 과정을 테스트하기 어려움
     *     특정 프레임워크 API 에 의존하게 됨
     *
     * [이벤트 사용 판단]
     * 직접 메소드 호출이 좋은 경우
     *     명시적인 처리 흐름이 있는 경우
     *     하나의 도메인에 속하는 경우
     *
     * 이벤트 처리가 좋은 경우
     *     여러 도메인에서 공통으로 사용되는 경우
     *     서로 다른 서비스로 분리될 가능성이 있는 경우
     *     순서 보장 없이 작업들을 처리해야 하는 경우
     *     주체적으로 어떤 사건의 발생을 인지하고 처리해야 하는 경우
     */
}
