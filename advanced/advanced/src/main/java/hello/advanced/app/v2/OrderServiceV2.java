package hello.advanced.app.v2;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 orderRepository;
    private final HelloTraceV2 trace;

    public void orderItem(TraceId traceId, String itemId) {

        TraceStatus status = null;

        try{
            //로그추적기-시작
            status = trace.beginSync(traceId,"OrderServiceV2.orderItem()");

            //비즈니스
            orderRepository.save(status.getTraceId(), itemId);

            //로그추적기-끝
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;//예외를 꼭 다시 던져주어야 한다.
            //로그 기능 때문에 흐름을 변경시키면 안된다.
        }
    }
}
