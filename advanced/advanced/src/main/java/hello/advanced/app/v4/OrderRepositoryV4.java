package hello.advanced.app.v4;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;
import hello.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV4 {

    private final LogTrace trace;

    public void save(String itemId) {

        AbstractTemplate<Void> template = new AbstractTemplate<Void>(trace) {
            @Override
            protected Void call() {
                //저장 로직
                if(itemId.equals("ex")) { //id에 ex가 포함되면 예외 발생
                    throw new IllegalStateException("예외 발생!");
                }
                sleep(1000); //상품을 주문하는데는 1초정도 걸린다고 가정
                return null;
            }
        };
        template.execute("OrderRepositoryV4.save()");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
