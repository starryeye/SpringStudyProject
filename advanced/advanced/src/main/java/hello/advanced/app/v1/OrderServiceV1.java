package hello.advanced.app.v1;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {

    private final OrderRepositoryV1 orderRepository;
    private final HelloTraceV1 trace;

    public void orderItem(String itemId) {

        TraceStatus status = null;

        try{
            //로그추적기-시작
            status = trace.begin("OrderServiceV1.orderItem()");

            //비즈니스
            orderRepository.save(itemId);

            //로그추적기-끝
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;//예외를 꼭 다시 던져주어야 한다.
            //로그 기능 때문에 흐름을 변경시키면 안된다.
        }
    }
}
