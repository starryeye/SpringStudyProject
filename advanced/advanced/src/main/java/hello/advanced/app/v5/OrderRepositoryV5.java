package hello.advanced.app.v5;

import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logtrace.LogTrace;
import hello.advanced.trace.template.AbstractTemplate;
import org.springframework.stereotype.Repository;

@Repository

public class OrderRepositoryV5 {

    private final TraceTemplate template;

    public OrderRepositoryV5(LogTrace trace) {
        this.template = new TraceTemplate(trace);
    }


    public void save(String itemId) {

        template.execute("OrderRepositoryV5.save()", () -> {
            //저장 로직
            if(itemId.equals("ex")) { //id에 ex가 포함되면 예외 발생
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000); //상품을 주문하는데는 1초정도 걸린다고 가정
            return null;
        });
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
