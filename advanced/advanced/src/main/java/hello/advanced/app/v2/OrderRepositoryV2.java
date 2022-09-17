package hello.advanced.app.v2;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV2 {

    private final HelloTraceV2 trace;

    public void save(TraceId traceId, String itemId) {

        TraceStatus status = null;

        try{
            //로그추적기-시작
            status = trace.beginSync(traceId,"OrderRepositoryV1.save()");

            //비즈니스
            //저장 로직
            if(itemId.equals("ex")) { //id에 ex가 포함되면 예외 발생
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000); //상품을 주문하는데는 1초정도 걸린다고 가정

            //로그추적기-끝
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;//예외를 꼭 다시 던져주어야 한다.
            //로그 기능 때문에 흐름을 변경시키면 안된다.
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
